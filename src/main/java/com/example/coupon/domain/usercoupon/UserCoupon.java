package com.example.coupon.domain.usercoupon;

import com.example.coupon.domain.coupon.Coupon;
import com.example.coupon.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 실제 유저에게 발급된 쿠폰
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDate issuedDate;

    private boolean used;

    public static UserCoupon issueTo(User user, Coupon coupon, LocalDate issuedDate) {
        return UserCoupon.builder()
                .user(user)
                .coupon(coupon)
                .issuedDate(issuedDate)
                .build();
    }

    @Builder
    private UserCoupon(Coupon coupon, User user, LocalDate issuedDate) {
        this.coupon = coupon;
        this.user = user;
        this.issuedDate = issuedDate;
        this.used = false;
    }

    // 쿠폰이 현재 사용 가능하다는걸 판단해야함
    public boolean isValid(LocalDate now) {
        return !used && coupon.isValid(now);
    }
}
