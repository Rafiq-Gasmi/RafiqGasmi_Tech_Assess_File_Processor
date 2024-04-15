package com.rafiqgasmi.techincal.assessment.fileservice.config;

import com.rafiqgasmi.techincal.assessment.fileservice.validator.ValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class IPAuthenticationFilterTest {

    @InjectMocks
    private IPAuthenticationFilter filter;

    @Mock
    private ValidationService validationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    public void testDoFilterInternal_WithValidIP() throws Exception {
        when(request.getRemoteAddr()).thenReturn("valid.ip.address");
        when(validationService.validateIP(anyString())).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_WithInvalidIP() throws Exception {
        when(request.getRemoteAddr()).thenReturn("invalid.ip.address");
        when(validationService.validateIP(anyString())).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Your IP is blocked due to country or ISP restrictions");
    }

    @Test
    public void testDoFilterInternal_WithSkipValidation() throws Exception {
        when(request.getRemoteAddr()).thenReturn("invalid.ip.address");
        when(validationService.validateIP(anyString())).thenReturn(false);
        filter.setSkipValidation(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

}