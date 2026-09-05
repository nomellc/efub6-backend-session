package com.practice.efubaccount.account.service;

import com.practice.efubaccount.account.domain.AccountDocument;
import com.practice.efubaccount.account.dto.response.AccountResponseDto;
import com.practice.efubaccount.account.dto.response.CreateAccountResponseDto;
import com.practice.efubaccount.account.dto.request.BioUpdateRequestDto;
import com.practice.efubaccount.account.dto.request.CreateAccountRequestDto;
import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.domain.AccountStatus;
import com.practice.efubaccount.account.repository.AccountRepository;
import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    //TODO: Redis 필요 필드 추가

    //TODO: MongoDB 필요 필드 추가

    //TODO: 초기화

    // 회원 단건 조회
    public AccountResponseDto getAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        return AccountResponseDto.from(account);
    }

    // 회원 생성
    @Transactional
    public CreateAccountResponseDto createAccount(CreateAccountRequestDto requestDto) {
        // 이메일 중복 검사
        if (accountRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 email입니다. " + requestDto.getEmail());
        }
        Account account = requestDto.toEntity();
        Account savedAccount = accountRepository.save(account);

        //TODO: Redis에 저장

        //TODO: Mongo DB에 저장

        return CreateAccountResponseDto.from(savedAccount);
    }

    //TODO: Redis에서 조회
    @Transactional(readOnly = true)
    public String findEmailByIdFromRedis(Long accountId) {
        return null;
    }

    //TODO: Mongo DB에서 조회
    public String findNicknameByIdFromMongo(Long id) {
        return null;
    }


    // 프로필(자기소개) 수정
    @Transactional
    public AccountResponseDto updateAccount(Long accountId, BioUpdateRequestDto requestDto) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        account.updateBio(requestDto.getBio());
        account.updateNickname(requestDto.getNickname());

        //TODO: Redis에서 nickname 업데이트

        //TODO: Mongo DB에서 nickname 업데이트


        return AccountResponseDto.from(account);
    }

    // 회원 논리적 삭제 (status 변경)
    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        account.changeStatus(AccountStatus.DEACTIVATED);
    }

    // 회원 물리적 삭제
    @Transactional
    public void physicalDeleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        //TODO: Redis에서 삭제

        //MySQL에서 삭제
        accountRepository.delete(account);

        //TODO: Mongo DB에서 삭제
    }

    @Transactional(readOnly=true)
    public Account findByAccountId(Long accountId) {
        return accountRepository.findByAccountId(accountId)
                .orElseThrow(()-> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional(readOnly=true)
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    //TODO: AccountDocument 조회 헬퍼 메소드

    //TODO: Account - Redis 저장 메소드

}