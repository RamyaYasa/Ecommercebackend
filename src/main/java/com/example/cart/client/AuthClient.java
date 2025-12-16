package com.example.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}"
)
public interface AuthClient {

    @GetMapping("/api/auth/extract/claims")
    String extractClaims(
            @RequestHeader("Authorization") String authorizationHeader
    );
}
