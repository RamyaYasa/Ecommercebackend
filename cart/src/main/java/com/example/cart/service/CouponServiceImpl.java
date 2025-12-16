package com.example.cart.service;

import com.example.cart.dto.CouponRequestDTO;
import com.example.cart.dto.CouponResponseDTO;
import com.example.cart.model.Coupon;
import com.example.cart.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private CouponResponseDTO toDto(Coupon c) {
        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setCode(c.getCode());
        dto.setDescription(c.getDescription());
        dto.setType(c.getType());
        dto.setValue(c.getValue());
        dto.setMaxDiscount(c.getMaxDiscount());
        dto.setActive(c.isActive());
        return dto;
    }

    @Override
    public CouponResponseDTO addCoupon(CouponRequestDTO req) {

        if (couponRepository.existsById(req.getCode())) {
            throw new IllegalArgumentException("Coupon already exists");
        }

        Coupon coupon = Coupon.builder()
                .code(req.getCode())
                .description(req.getDescription())
                .type(req.getType())
                .value(req.getValue())
                .maxDiscount(req.getMaxDiscount())
                .active(req.isActive())
                .build();

        return toDto(couponRepository.save(coupon));
    }

    @Override
    public CouponResponseDTO updateCoupon(String code, CouponRequestDTO req) {

        Coupon coupon = couponRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

        coupon.setDescription(req.getDescription());
        coupon.setType(req.getType());
        coupon.setValue(req.getValue());
        coupon.setMaxDiscount(req.getMaxDiscount());
        coupon.setActive(req.isActive());

        return toDto(couponRepository.save(coupon));
    }

    @Override
    public void deleteCoupon(String code) {
        couponRepository.deleteById(code);
    }

    @Override
    public CouponResponseDTO getCoupon(String code) {
        return couponRepository.findById(code)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
    }

    @Override
    public List<CouponResponseDTO> getAllCoupons() {
        return couponRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
}
