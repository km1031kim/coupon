package com.example.coupon.controller;

import com.example.coupon.domain.coupon.CouponResponse;
import com.example.coupon.domain.coupon.CreateCouponRequest;
import com.example.coupon.domain.filemeta.FileResponse;
import com.example.coupon.service.CouponService;
import com.example.coupon.service.FileService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final FileService fileService;

    @Autowired
    public CouponController(CouponService couponService, FileService excelService) {
        this.couponService = couponService;
        this.fileService = excelService;
    }

    // 쿠폰 생성
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateCouponRequest request) {

        CouponResponse couponResponse = couponService.create(request);
        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "coupon successfully created",
                        "result", couponResponse
                )
        );
    }

    // csv, 엑셀을 통해 유저에게 쿠폰 발급
    @PostMapping(value = "/{couponId}/issues", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> issueCouponToUser(
            @PathVariable("couponId") long couponId,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 파일 저장
        FileResponse fileResponse = fileService.saveAndUpload(file);

        // 유저에게 쿠폰 발급
        couponService.issueCouponsTo(couponId, fileResponse);

        return ResponseEntity.ok().body(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "file successfully uploaded",
                        "result", fileResponse
                )
        );

    }
}
