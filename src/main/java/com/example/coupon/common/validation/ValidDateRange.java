package com.example.coupon.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE}) // 해당 어노테이션을 어디에 붙일 수 있는지
@Retention(RetentionPolicy.RUNTIME) // 어노테이션이 언제까지 살아있을 지. 런타임 시점까지 어노테이션 정보 유지.
@Constraint(validatedBy = DateRangeValidator.class) // 검증 어노테이션임을 명시. 실제로 유효성 검사를 수행할 클래스 지정
@Documented
public @interface ValidDateRange {
    String message() default "시작일은 종료일과 같거나 이전이어야 합니다."; // 유효성 검사 실패 시 출력할 메세지
    Class<?>[] groups() default {}; // 특정 밸리데이션을 그룹으로 묶어서 유효성 검사를 나눠서 실행할 수 있도록 함. 기본값/
    Class<? extends Payload>[] payload() default {}; // 추가 메타데이터 제공을 위한 속성
}
