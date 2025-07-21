package com.example.coupon.domain.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 쿠폰 메타데이터
 * 쿠폰 정보를 바탕으로 유저쿠폰이 발급됨.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 쿠폰 이름
    @Column(unique = true)
    private String name;

    // 할인율 (예: 10%는 0.1로 저장)
    private double discountRate;

    // 유효 기간 시작일
    private LocalDate startDate;

    // 유효 기간 종료일
    private LocalDate endDate;

    /**
     *     // 적용 대상 카테고리 (예: "음료", "디저트" 등)
     *     private String targetCategory;
     */

    @Builder
    public Coupon(String name, double discountRate, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.discountRate = discountRate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isValid(LocalDate now) {
        return !now.isAfter(endDate); // 오늘 <= 마감일
    }
}
