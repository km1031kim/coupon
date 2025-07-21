package com.example.coupon.domain.coupon;

import com.example.coupon.common.validation.ValidDateRange;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ValidDateRange
public class CreateCouponRequest {

    @NotBlank(message = "쿠폰 이름은 필수값입니다.")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "할인율은 0보다 커야합니다.")
    @DecimalMax(value = "1.0", inclusive = true, message = "할인율은 1보다 작아야합니다.")
    private double discountRate;

    @NotNull(message = "시작일은 필수입니다.")
    @FutureOrPresent(message = "쿠폰 적용 시작일은 오늘 이후여야 합니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    @FutureOrPresent(message = "쿠폰 적용 마감일은 오늘 이후여야 합니다.")
    private LocalDate endDate;

}
