package com.example.coupon.controller;

import com.example.coupon.domain.user.CreateUserRequest;
import com.example.coupon.domain.user.UserResponse;
import com.example.coupon.domain.user.UserSearchRequest;
import com.example.coupon.domain.user.UserUpdateRequest;
import com.example.coupon.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원가입
    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequest createUserRequest) {

        UserResponse userResponse = userService.register(createUserRequest);

        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "user successfully created",
                        "result", userResponse)
        );
    }

    // 모든 유저 조회
    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<UserResponse> page = userService.findAll(pageable);

        return ResponseEntity.ok().body(Map.of(
                "data", page.getContent(),
                "pagination", Map.of(
                        "page", page.getNumber(),
                        "size", page.getSize(),
                        "totalPages", page.getTotalPages(),
                        "totalElements", page.getTotalElements(),
                        "isLast", page.isLast()
                )
        ));
    }

    // 이메일로 유저 조회
    @GetMapping("/search")
    public ResponseEntity<?> getUserByEmail(@ModelAttribute @Valid UserSearchRequest request) {
        // ModelAttribute는 내부적으로 setter 사용
        log.info("request : {}", request.getEmail());
        UserResponse userResponse = userService.findUserBy(request.getEmail());

        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "result", userResponse)
        );
    }

    // 유저정보변경
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserInfo(@PathVariable("userId") Long userId, @Valid @RequestBody UserUpdateRequest request) {

        UserResponse userResponse = userService.updateUserBy(userId, request);

        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "user info successfully changed",
                        "result", userResponse)
        );
    }

    /**
     * 회원 비활성화
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deactivateUser(@PathVariable("userId") Long userId) {

        UserResponse userResponse = userService.deactivateUserBy(userId);

        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "user deactivated",
                        "result", userResponse)
        );

    }

    /**
     * 회원 활성화
     * @param userId
     * @return
     */
    @PatchMapping("/{userId}/activation")
    public ResponseEntity<?> activateUser(@PathVariable("userId") Long userId) {

        UserResponse userResponse = userService.activateUserBy(userId);

        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "user activate",
                        "result", userResponse)
        );

    }

}
