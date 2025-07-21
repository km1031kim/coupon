package com.example.coupon.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private final long id; // 둘다 고유값인데.. 인덱싱에 따라 필요 여부가 결정될 듯 하다.
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final boolean active;


    @Builder
    public UserResponse(long id, String email, String name, String phoneNumber, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public static UserResponse from(User savedUser) {
        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .phoneNumber(savedUser.getPhoneNumber())
                .active(savedUser.isActive())
                .build();
    }
}
