package com.techelevator.tenmo.services;

import org.springframework.web.client.RestTemplate;

public class AppService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AppService(String url) {
        this.baseUrl = url;
    }


}
