package com.datadoghq.workshops.samplevulnerablejavaapp.service;

import com.datadoghq.workshops.samplevulnerablejavaapp.http.WebsiteTestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WebsiteTestService {
    @Autowired
    private RestTemplate rest;

    private boolean isValidUrl(String url) {
        // Implement URL validation logic here
        // For example, check if the URL starts with a specific prefix
        return url.startsWith("http://allowed-domain.com") || url.startsWith("https://allowed-domain.com");
    }

    public String testWebsite(WebsiteTestRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (request.customHeaderKey != null && !request.customHeaderKey.isEmpty()) {
                headers.set(request.customHeaderKey, request.customHeaderValue);
            }

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            if (isValidUrl(request.url)) {
                return this.rest.exchange(request.url, HttpMethod.GET, entity, String.class).getBody();
            } else {
                return "Invalid URL";
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "URL returned status code: " + e.getStatusCode();
        }
    }
}
