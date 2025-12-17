package com.login.auth.client;



import org.springframework.stereotype.Component;

@Component
public class CustomFeignContext {
    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public void setToken(String token) {
        TOKEN.set(token);
    }

    public String getToken() {
        return TOKEN.get();
    }

    public void clear() {
        TOKEN.remove();
    }

    // Helper method to extract token without "Bearer " prefix if needed
    public String getTokenWithoutBearer() {
        String token = getToken();
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}