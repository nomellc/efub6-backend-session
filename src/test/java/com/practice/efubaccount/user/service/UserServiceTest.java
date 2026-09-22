package com.practice.efubaccount.user.service;

import com.practice.efubaccount.user.dto.UserRequestDTO;
import com.practice.efubaccount.user.entity.Role;
import com.practice.efubaccount.user.entity.User;
import com.practice.efubaccount.user.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // TODO 7. UserRepository를 Mock 객체로 생성해주세요.

    // TODO 8. Mock 객체를 주입받는 UserService를 생성해주세요.

    private Validator validator;
    private User testUser;

    // TODO 9. 각 테스트 실행 전에
    // 1) Validator를 생성하고
    // 2) 테스트용 User 객체를 초기화해주세요.


    // TODO 10. 중복 이메일이 존재하는 경우
    // IllegalArgumentException이 발생하는지 테스트해주세요.
    // 또한 repository.save()가 호출되지 않았는지 검증해주세요.


    // TODO 11. 일반 사용자가 회원 삭제를 시도하면
    // IllegalArgumentException이 발생하는지 테스트해주세요.
    // 또한 deleteById()가 호출되지 않았는지 검증해주세요.


    // TODO 12. @ParameterizedTest를 사용하여
    // 잘못된 이메일 형식들을 반복 검증해주세요.


    // TODO 13. id로 사용자를 조회했을 때
    // Repository가 반환한 User의 name과 email이 올바른지 검증해주세요.
}
