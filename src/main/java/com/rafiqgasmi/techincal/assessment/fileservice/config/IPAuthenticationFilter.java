package com.rafiqgasmi.techincal.assessment.fileservice.config;

import com.rafiqgasmi.techincal.assessment.fileservice.validator.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class IPAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private ValidationService validationService;


    @Value("${skip.validation:false}")
    private boolean skipValidation;

    public void setSkipValidation(boolean skipValidation) {
        this.skipValidation = skipValidation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestIp = request.getRemoteAddr();

        if (!skipValidation) {
            if (!validationService.validateIP(requestIp)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your IP is blocked due to country or ISP restrictions");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}