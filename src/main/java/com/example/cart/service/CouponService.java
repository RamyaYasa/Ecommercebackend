package com.example.cart.service;

import com.example.cart.dto.CouponRequestDTO;
import com.example.cart.dto.CouponResponseDTO;

import java.util.List;

public interface CouponService {

    CouponResponseDTO addCoupon(CouponRequestDTO req);

    CouponResponseDTO updateCoupon(String code, CouponRequestDTO req);

    void deleteCoupon(String code);

    CouponResponseDTO getCoupon(String code);

    List<CouponResponseDTO> getAllCoupons();
}

