package com.rafiqgasmi.techincal.assessment.fileservice.logging;

import com.rafiqgasmi.techincal.assessment.fileservice.domain.RequestLog;
import com.rafiqgasmi.techincal.assessment.fileservice.repository.RequestLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureMockMvc
class LoggingInterceptorTest {

    @Mock
    private RequestLogRepository requestLogRepository;

    @InjectMocks
    private LoggingInterceptor loggingInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    //tests the log is inserted to the database
    @Test
    public void afterCompletion_saveFails() throws Exception {

    }
}