package com.login.auth.client;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final CustomFeignContext customFeignContext;

    @Override
    public void apply(RequestTemplate template) {
        // Try to get token from the current HTTP request first
        String tokenFromRequest = extractTokenFromRequest();

        // If not found in request, try from context
        String token = (tokenFromRequest != null) ? tokenFromRequest : customFeignContext.getToken();

        log.info("Feign Client Interceptor - Token found: {}", token != null);

        if (token != null) {
            template.header("Authorization", token);
            log.debug("Forwarding token in Feign request to: {}", template.url());
        } else {
            log.warn(" No token found to forward in Feign request for URL: {}", template.url());
        }
    }

    private String extractTokenFromRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader;
            }
        }
        return null;
    }
}
