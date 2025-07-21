package com.example.coupon.service;

import com.example.coupon.domain.coupon.Coupon;
import com.example.coupon.domain.coupon.CouponResponse;
import com.example.coupon.domain.coupon.CreateCouponRequest;
import com.example.coupon.domain.filemeta.FileResponse;
import com.example.coupon.repository.CouponRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public CouponResponse create(CreateCouponRequest request) {

        // 쿠폰 이름 중복 확인
        String couponName = request.getName();
        checkDuplicatedCoupon(couponName);

        Coupon coupon = Coupon.builder()
                .name(couponName)
                .discountRate(request.getDiscountRate())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        return CouponResponse.from(savedCoupon);
    }

    private void checkDuplicatedCoupon(String couponName) {
        if (couponRepository.existsByName(couponName)) {
            throw new EntityExistsException("이미 존재하는 쿠폰명입니다. 다른 이름으로 시도해주세요.");
        };
    }

    @Transactional
    public void issueCouponsTo(long couponId, FileResponse targetUserList) {

    }
}
