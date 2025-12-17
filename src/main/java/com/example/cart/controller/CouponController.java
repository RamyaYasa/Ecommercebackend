package com.example.cart.controller;

import com.example.cart.client.AuthClient;

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
    private final AuthClient authClient;

    // ADMIN: Add coupon
    @PostMapping("/add")
    public CouponResponseDTO addCoupon(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CouponRequestDTO req) {
        String userId = authClient.extractClaims(authorizationHeader);
        return couponService.addCoupon(req);
    }

    // ADMIN: Update coupon
    @PutMapping("/update/{code}")
    public CouponResponseDTO updateCoupon(
            @RequestHeader("AuthorizationHeader") String authorizationHeader,
            @PathVariable String code,
            @RequestBody CouponRequestDTO req) {
        String userId = authClient.extractClaims(authorizationHeader);
        return couponService.updateCoupon(code, req);
    }

    // ADMIN: Delete coupon
    @DeleteMapping("/delete/{code}")
    public void deleteCoupon(
            @RequestHeader("AuthorizationHeader") String authorizationHeader,
            @PathVariable String code) {
        String userId = authClient.extractClaims(authorizationHeader);
        couponService.deleteCoupon(code);
    }

    // ADMIN: Get single coupon
    @GetMapping("/{code}")
    public CouponResponseDTO getCoupon(
            @RequestHeader("AuthorizationHeader") String authorizationHeader,
            @PathVariable String code) {
        String userId = authClient.extractClaims(authorizationHeader);
        return couponService.getCoupon(code);
    }

    // USER: Get all coupons
    @GetMapping("/all")
    public List<CouponResponseDTO> getAllCouponsForUser(
            @RequestHeader("AuthorizationHeader") String authorizationHeader) {
        String userId = authClient.extractClaims(authorizationHeader);
        return couponService.getAllCoupons();
    }
}

