package com.rafiqgasmi.techincal.assessment.fileservice.logging;

import com.rafiqgasmi.techincal.assessment.fileservice.domain.GlobalIpApiResponse;
import com.rafiqgasmi.techincal.assessment.fileservice.domain.RequestLog;
import com.rafiqgasmi.techincal.assessment.fileservice.repository.RequestLogRepository;
import com.rafiqgasmi.techincal.assessment.fileservice.validator.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    //interceptor is invoked at three stages
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);


    @Autowired
    private RequestLogRepository requestLogRepository;


    //it's used to record the start time of the request.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    //add additional attributes before the view is rendered
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // This method is intentionally left empty
    }

    //after the request has been fully processed and the view has been rendered
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long elapsed = System.currentTimeMillis() - startTime;
        String status = String.valueOf(response.getStatus());
        LocalDateTime currentDateTime = LocalDateTime.now();
        logRequestDetails(UUID.randomUUID().toString(), request.getRequestURI(), currentDateTime, status, request.getRemoteAddr(), request.getLocale().getCountry(), GlobalIpApiResponse.getInstance().getIpApiResponse().getIsp(), elapsed);
        logRequestToDatabase(UUID.randomUUID().toString(), request.getRequestURI(), currentDateTime, status, request.getRemoteAddr(), request.getLocale().getCountry(), GlobalIpApiResponse.getInstance().getIpApiResponse().getIsp(), elapsed);
    }


    private void logRequestDetails(String requestId, String requestUri, LocalDateTime requestTimestamp, String httpResponseCode, String requestIpAddress, String countryCode, String ipProvider, Long timeLapsed) {
        log.info("Request Details - ID: {}, URI: {}, Timestamp: {}, HTTP Response Code: {}, IP Address: {}, Country Code: {}, IP Provider: {}, Time Lapsed: {}",
                requestId, requestUri, requestTimestamp.toString(), httpResponseCode, requestIpAddress, countryCode, ipProvider, timeLapsed);
    }

    private void logRequestToDatabase(String requestId, String requestUri, LocalDateTime requestTimestamp, String httpResponseCode, String requestIpAddress, String countryCode, String ipProvider, Long timeLapsed) {
        RequestLog requestLog = RequestLog.builder()
                .requestId(requestId)
                .requestUri(requestUri)
                .requestTimestamp(requestTimestamp)
                .httpResponseCode(httpResponseCode)
                .requestIpAddress(requestIpAddress)
                .countryCode(countryCode)
                .ipProvider(ipProvider)
                .timeLapsed(timeLapsed)
                .build();

        try {
            requestLogRepository.save(requestLog);
        } catch (Exception e) {
            log.error("Failed to save request log to database", e);
        }
    }
}
