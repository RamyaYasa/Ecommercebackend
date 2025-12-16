package com.example.cart.controller;

import com.example.cart.dto.CouponRequestDTO;
import com.example.cart.dto.CouponResponseDTO;
import com.example.cart.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // ADMIN: Add coupon
    @PostMapping("/add")
    public CouponResponseDTO addCoupon(@RequestBody CouponRequestDTO req) {
        return couponService.addCoupon(req);
    }

    // ADMIN: Update coupon
    @PutMapping("/update/{code}")
    public CouponResponseDTO updateCoupon(
            @PathVariable String code,
            @RequestBody CouponRequestDTO req) {
        return couponService.updateCoupon(code, req);
    }

    // ADMIN: Delete coupon
    @DeleteMapping("/delete/{code}")
    public void deleteCoupon(@PathVariable String code) {
        couponService.deleteCoupon(code);
    }

    // ADMIN: Get single coupon
    @GetMapping("/{code}")
    public CouponResponseDTO getCoupon(@PathVariable String code) {
        return couponService.getCoupon(code);
    }

    // USER: Get all coupons
    @GetMapping("/all")
    public List<CouponResponseDTO> getAllCouponsForUser() {
        return couponService.getAllCoupons();
    }
}

