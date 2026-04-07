package com.practice.efubaccount.post.service;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.service.AccountsService;
import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import com.practice.efubaccount.post.domain.Post;
import com.practice.efubaccount.post.dto.request.PostCreateRequest;
import com.practice.efubaccount.post.dto.request.PostUpdateRequest;
import com.practice.efubaccount.post.dto.response.PostListResponse;
import com.practice.efubaccount.post.dto.response.PostResponse;
import com.practice.efubaccount.post.dto.summary.PostSummary;
import com.practice.efubaccount.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final AccountsService accountsService;
    private final PostRepository postRepository;


    @Transactional
    public Long createPost(@Valid PostCreateRequest request) {
        Account writerAccount = accountsService.findByAccountId(request.getAccountId());

        Post newPost = request.toEntity(writerAccount);
        postRepository.save(newPost);
        return newPost.getId();

    }

    @Transactional(readOnly = true)
    public PostListResponse getAllPost() { //stream쓰면 list가 하나씩 쪼개짐
        List<PostSummary> postSummaries = postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostSummary::from)
                .toList();
        return new PostListResponse(postSummaries, postRepository.count());
    }

    @Transactional //조회수 업데이트 필요해서 readOnly 안 씀
    public PostResponse getPost(Long postId) {
        //조회수 증가
        postRepository.increaseViewCount(postId);

        Post post = findByPostId(postId);
        return PostResponse.from(post);
    }

    @Transactional
    public void updatePostContent(Long postId, Long accountId, @Valid PostUpdateRequest request) {
        Post post = findByPostId(postId);
        Account account = accountsService.findByAccountId(accountId);

        //로그인 한 사람이 포스트 작성자인지 확인하는 코드 필요 -> 공통 메소드로 빼기
        authorizePostWriter(post, account);

        post.changeContent(request.content());

    }

    @Transactional
    public void deletePost(Long postId, Long accountId) {
        Post post = findByPostId(postId);
        Account account = accountsService.findByAccountId(accountId);

        authorizePostWriter(post, account);

        postRepository.delete(post);

    }


    public Post findByPostId(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void authorizePostWriter(Post post, Account account){
        if(!post.getWriter().equals(account)){
            throw  new CustomException(ErrorCode.POST_ACCOUNT_MISMATCH);
        }
    }


}
