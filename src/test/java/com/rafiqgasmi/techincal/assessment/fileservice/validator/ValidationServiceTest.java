package com.rafiqgasmi.techincal.assessment.fileservice.validator;

import com.rafiqgasmi.techincal.assessment.fileservice.domain.IpApiResponse;
import com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions;
import com.rafiqgasmi.techincal.assessment.fileservice.validator.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ValidationServiceTest {

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private final ValidationService validationService = new ValidationService();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(validationService, "blockedCountries", "US,CA");
        ReflectionTestUtils.setField(validationService, "blockedIsps", "ISP1,ISP2");
    }



    @Test
    public void getIpApiResponseTest_success() {
        IpApiResponse ipApiResponse = new IpApiResponse();
        ipApiResponse.setStatus("success");
        ResponseEntity<IpApiResponse> responseEntity = new ResponseEntity<>(ipApiResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        IpApiResponse result = validationService.getIpApiResponse("152.119.13.249");

        assertEquals("success", result.getStatus());
    }

    @Test
    public void getIpApiResponseTest_fail() {
        IpApiResponse ipApiResponse = new IpApiResponse();
        ipApiResponse.setStatus("fail");
        ResponseEntity<IpApiResponse> responseEntity = new ResponseEntity<>(ipApiResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        IpApiResponse result = validationService.getIpApiResponse("192.168.0.1");
        assertEquals("fail", result.getStatus());
    }




    @Test
    public void validateIPTest() {
        ResponseEntity<IpApiResponse> responseEntity = createResponseEntity("US", "Test ISP", HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);
        boolean result = validationService.validateIP("152.119.13.249");

        assertTrue(result);
    }


    @Test
    public void validateIPTest_nullIp() {
        boolean result = validationService.validateIP(null);
        assertFalse(result);
    }

    //if "http://ip-api.com/json/" is given no input it will use you own IP
    @Test
    public void validateIPTest_emptyIp() {
        boolean result = validationService.validateIP("");
        assertTrue(result);
    }

    @Test
    public void validateIPTest_invalidIp_throwsException() {
        boolean result = validationService.validateIP("invalid-ip");
        assertFalse(result);

    }



    private ResponseEntity<IpApiResponse> createResponseEntity(String country, String as, HttpStatus status) {
        IpApiResponse ipApiResponse = new IpApiResponse();
        ipApiResponse.setCountry(country);
        ipApiResponse.setAs(as);

        return new ResponseEntity<>(ipApiResponse, status);
    }
}