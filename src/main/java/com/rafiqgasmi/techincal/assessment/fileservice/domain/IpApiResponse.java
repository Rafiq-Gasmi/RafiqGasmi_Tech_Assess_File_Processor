package com.rafiqgasmi.techincal.assessment.fileservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpApiResponse {
    private String country;
    private String as;
    private String isp;
    private String asname;
    private String org;
    private String status;
    private String message;
    private String query;
    private String countryCode;

}

/*
*  "query": "192.168.0.0",
  "status": "success",
  "country": "United Kingdom",
  "countryCode": "GB",
  "region": "ENG",
  "regionName": "England",
  "city": "Tower Hamlets",
  "zip": "E3",
  "lat": 00.00,
  "lon": -0.00,
  "timezone": "Europe/London",
  "isp": "Virgin Media Limited",
  "org": "Vmcbbuk",
  "as": "AS5089 Virgin Media Limited"
  *
  *
  *
  *
  "query": "d",
  "message": "invalid query",
  "status": "fail"
  * */