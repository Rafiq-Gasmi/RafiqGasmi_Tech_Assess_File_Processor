package com.rafiqgasmi.techincal.assessment.fileservice.domain;

import com.rafiqgasmi.techincal.assessment.fileservice.domain.IpApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalIpApiResponse {
    private static GlobalIpApiResponse instance;
    private IpApiResponse ipApiResponse;

    private GlobalIpApiResponse() {}

    public static synchronized GlobalIpApiResponse getInstance() {
        if (instance == null) {
            instance = new GlobalIpApiResponse();
        }
        return instance;
    }

}