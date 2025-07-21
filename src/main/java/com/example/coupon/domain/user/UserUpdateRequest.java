package com.example.coupon.domain.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "정확한 이메일 형식을 입력하세요."
    )
    private String email;

    @Pattern(
            regexp = "^[가-힣a-zA-Z]{2,20}$",
            message = "이름은 공백 없이 한글 또는 영문 2~20자여야 합니다."
    )
    private String name;

    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;

}
