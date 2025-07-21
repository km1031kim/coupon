package com.example.coupon.domain.coupon;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CouponResponse {

    private Long id;

    private String name;

    private double discountRate;

    private LocalDate startDate;

    private LocalDate endDate;


    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .discountRate(coupon.getDiscountRate())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .build();
    }
}
