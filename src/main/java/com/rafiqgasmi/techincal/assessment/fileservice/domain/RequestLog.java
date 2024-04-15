package com.rafiqgasmi.techincal.assessment.fileservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String requestId;
    private String requestUri;
    private LocalDateTime requestTimestamp;
    private String httpResponseCode;
    private String requestIpAddress;
    private String countryCode;
    private String ipProvider;
    private Long timeLapsed;

}