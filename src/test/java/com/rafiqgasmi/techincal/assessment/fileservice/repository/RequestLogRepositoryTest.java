package com.rafiqgasmi.techincal.assessment.fileservice.repository;


import com.rafiqgasmi.techincal.assessment.fileservice.domain.RequestLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RequestLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Test
    public void saveTest() {
        RequestLog requestLog = RequestLog.builder()
                .requestId("1")
                .requestUri("/test")
                .requestTimestamp(LocalDateTime.now())
                .httpResponseCode("200")
                .requestIpAddress("127.0.0.1")
                .countryCode("US")
                .ipProvider("Test Provider")
                .timeLapsed(1000L)
                .build();

        requestLogRepository.save(requestLog);

        RequestLog found = entityManager.find(RequestLog.class, requestLog.getId());

        assertEquals(requestLog, found);
    }
}