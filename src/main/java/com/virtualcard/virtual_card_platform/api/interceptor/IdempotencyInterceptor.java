package com.virtualcard.virtual_card_platform.api.interceptor;

import com.virtualcard.virtual_card_platform.application.service.IdempotencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private final IdempotencyService service;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String key = request.getHeader("Idempotency-Key");

        if (key == null || key.isBlank()) {
            return true;
        }

        String endpoint = request.getRequestURI();

        var existing = service.find(key, endpoint);

        if (existing.isPresent()) {

            response.setStatus(existing.get().getHttpStatus());
            response.setContentType("application/json");
            response.getWriter().write(existing.get().getResponseBody());

            return false;
        }

        return true;
    }
}


