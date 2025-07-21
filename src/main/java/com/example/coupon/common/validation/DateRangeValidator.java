package com.example.coupon.common.validation;

import com.example.coupon.domain.coupon.CreateCouponRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, CreateCouponRequest> {

    @Override
    public boolean isValid(CreateCouponRequest request, ConstraintValidatorContext context) {

        boolean isValid = !request.getEndDate().isBefore(request.getStartDate());

        if (!isValid) {
            // 클래스 레벨 제약 조건에서 발생한 유효성 실패 메세지를 특정 필드에 바인딩
           context.disableDefaultConstraintViolation(); // 기본 메세지 출력 막기
            context.buildConstraintViolationWithTemplate("시작일은 종료일보다 이전이어야 합니다.")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
