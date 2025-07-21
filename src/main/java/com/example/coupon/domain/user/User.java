package com.example.coupon.domain.user;

import com.example.coupon.domain.coupon.Coupon;
import com.example.coupon.domain.usercoupon.UserCoupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa는 기본 생성자로 엔티티 생성 후 값 주입
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;


    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private boolean active;

    // 유저는 쿠폰 목록을 자주 조회한다 => 양방향
    @OneToMany(mappedBy = "user") // mappedBy 로 연관관계의 주인이 아님을 선언. user는 UserCoupon 클래스 안에 있는 User 필드명을 칭함.
    List<UserCoupon> userCoupons;


    @Builder
    public User(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.active = true;
        this.userCoupons = new ArrayList<>();
    }

    public void changeUsername(String name) {
        this.name = name;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeEmail(String email) {
        this.email = email;
    }


    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void addCoupons(UserCoupon userCoupon) {
        userCoupons.add(userCoupon);
    }
}
