package kuit3.backend.controller;

import kuit3.backend.common.argument_resolver.PreAuthorize;
import kuit3.backend.common.exception.UserException;
import kuit3.backend.common.response.BaseResponse;
import kuit3.backend.dto.auth.LoginRequest;
import kuit3.backend.dto.auth.LoginResponse;
import kuit3.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kuit3.backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static kuit3.backend.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Validated @RequestBody LoginRequest authRequest, BindingResult bindingResult) {
        log.info("[AuthController.login]");
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(authService.login(authRequest));
    }

    /**
     * 인가(JWT 검증) 테스트
     */
    @GetMapping("/test")
    public BaseResponse<String> checkAuthorization(@PreAuthorize long userId) {
        log.info("[AuthController.checkAuthorization]");
        return new BaseResponse<>("userId=" + userId);
    }

}