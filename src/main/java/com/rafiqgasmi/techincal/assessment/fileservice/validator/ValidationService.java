package com.rafiqgasmi.techincal.assessment.fileservice.validator;

import com.rafiqgasmi.techincal.assessment.fileservice.domain.GlobalIpApiResponse;
import com.rafiqgasmi.techincal.assessment.fileservice.domain.IpApiResponse;
import com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions;
import com.rafiqgasmi.techincal.assessment.fileservice.logging.LoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions.*;

@Service
@PropertySource("classpath:validation.properties")
public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);

    @Value("${blocked.countries:}")
    private String blockedCountries;

    @Value("${blocked.isps:}")
    private String blockedIsps;


    public IpApiResponse ipApiResponse; // global variable

    public IpApiResponse getIpApiResponse(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://ip-api.com/json/" + ip;
        ResponseEntity<IpApiResponse> response = restTemplate.getForEntity(url, IpApiResponse.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            GlobalIpApiResponse.getInstance().setIpApiResponse(response.getBody());
            return GlobalIpApiResponse.getInstance().getIpApiResponse();
        }
        throw new BadRequestException ("Error occurred while calling the API");
    }

    public boolean validateIP(String ip) {
        List<String> BLOCKED_COUNTRIES = Arrays.asList(blockedCountries.split(","));
        List<String> BLOCKED_ISPS = Arrays.asList(blockedIsps.split(","));

        IpApiResponse ipApiResponse = getIpApiResponse(ip);
        if (ipApiResponse != null && "success".equals(ipApiResponse.getStatus())) {
            return !BLOCKED_COUNTRIES.contains(ipApiResponse.getCountry()) && !BLOCKED_ISPS.contains(ipApiResponse.getAs());
        }
        return false;
    }

}
