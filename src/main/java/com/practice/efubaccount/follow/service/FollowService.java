package com.practice.efubaccount.follow.service;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.service.AccountService;
import com.practice.efubaccount.follow.domain.Follow;
import com.practice.efubaccount.follow.dto.FollowListResponse;
import com.practice.efubaccount.follow.dto.FollowStatusResponse;
import com.practice.efubaccount.follow.enums.FollowStatus;
import com.practice.efubaccount.follow.repository.FollowRepository;
import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final AccountService accountService;
    private final FollowRepository followRepository;

    // 팔로우 추가
    @Transactional
    public FollowStatusResponse follow(Long requesterId, Long targetId) {
        if (requesterId.equals(targetId)) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        Account requester = accountService.findByAccountId(requesterId);
        Account target = accountService.findByAccountId(targetId);

        if (followRepository.existsByFollowerAndFollowee(requester, target)) {
            throw new CustomException(ErrorCode.FOLLOW_ALREADY_EXISTS);
        }

        Follow follow = Follow.builder()
                .follower(requester)
                .followee(target)
                .build();
        followRepository.save(follow);

        return FollowStatusResponse.of(target, FollowStatus.FOLLOW);
    }

    // 팔로우 취소
    @Transactional
    public FollowStatusResponse unfollow(Long requesterId, Long targetId) {
        Account requester = accountService.findByAccountId(requesterId); // find는 값이 없을 수도 있는 것을 대상으로 함
        Account target = accountService.findByAccountId(targetId); // null 대비 필요

        Follow follow = getFollow(requester, target); // get은 값이 있다는 것을 암묵적으로 내포

        followRepository.delete(follow);

        return FollowStatusResponse.of(target, FollowStatus.UNFOLLOW);
    }

    // 팔로우 여부 조회
    public FollowStatusResponse checkFollowStatusByEmail(Long requesterId, String targetEmail) {
        Account requester = accountService.findByAccountId(requesterId);
        Account target = accountService.findByEmail(targetEmail);

        boolean isFollowing = followRepository.existsByFollowerAndFollowee(requester, target);
        FollowStatus status = isFollowing ? FollowStatus.FOLLOW : FollowStatus.UNFOLLOW;

        return FollowStatusResponse.of(target, status);
    }


    // 팔로우 목록 조회
    public FollowListResponse getFollowList(Long requesterId) {
        // 쿼리 1번
        Account requester = accountService.findByAccountId(requesterId);

        // 요청자가 followee로 있는 follow 객체들
        // 쿼리 1번
        List<Follow> followerList = followRepository.findAllByFollowee(requester);

        // 요청자가 follower로 있는 follow 객체들
        // 쿼리 1번
        List<Follow> followingList = followRepository.findAllByFollower(requester);

        // 가져온 follow 객체에서 follower만 추출해 리스트로 생성
        List<Account> followers = followerList.stream()
                .map(Follow::getFollower).toList();
        List<Account> followings = followingList.stream()
                .map(Follow::getFollowee).toList();

        return FollowListResponse.of(followers, followings);
    }

    // --- private helper methods
    private Follow getFollow(Account follower, Account followee) {
        return followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));
    }
}
