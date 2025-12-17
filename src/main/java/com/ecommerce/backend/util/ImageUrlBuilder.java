package com.ecommerce.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUrlBuilder {

    @Value("${app.base-url}")
    private String baseUrl;

    public String buildImageUrl(String tableName, Long id) {
        return baseUrl + "/api/admin/" + tableName + "/image/" + id;
    }
}
