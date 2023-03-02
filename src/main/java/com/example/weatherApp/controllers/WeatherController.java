package com.example.weatherApp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherController {
	private final String RAPID_API_HOST = "forecast9.p.rapidapi.com";
    private final String RAPID_API_FORECAST_SUMMARY_PATH = "/weather";
    private final String RAPID_API_HOURLY_FORECAST_PATH = "/forecast";

    @Value("${rapidapi.key:67580a5c08mshf65bfbcc5f9936cp1de5aajsn1d6e76d9d692}")
    private String rapidApiKey;

    @Value("${rapidapi.client-id:uesdkzsqccjkpxkVc6c49fLvz7sNXshpFmIdXjBxOVAeC6QD9U}")
    private String clientId;

    @Value("${rapidapi.client-secret:uesdkzsqccjkpxkVc6c49fLvz7sNXshpFmIdXjBxOVAeC6QD9U}")
    private String clientSecret;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @GetMapping("/forecast-summary")
    public ResponseEntity<String> getForecastSummary(@RequestParam String city,
                                                      @RequestHeader HttpHeaders headers,
                                                      RestTemplate restTemplate) {
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", city);

        ResponseEntity<String> response = restTemplate.exchange(
                //buildUrl(RAPID_API_HOST, RAPID_API_FORECAST_SUMMARY_PATH, queryParams),
        		"https://engv4dw1zg9bu.x.pipedream.net",
        		HttpMethod.GET,
                buildRequestEntity(headers),
                String.class);

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/hourly-forecast")
    public ResponseEntity<String> getHourlyForecast(@RequestParam String city,
                                                    @RequestHeader HttpHeaders headers,
                                                    RestTemplate restTemplate) {
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", city);

        ResponseEntity<String> response = restTemplate.exchange(
                //buildUrl(RAPID_API_HOST, RAPID_API_HOURLY_FORECAST_PATH, queryParams),
                buildUrl(RAPID_API_HOST, "", queryParams),
        		HttpMethod.GET,
                buildRequestEntity(headers),
                String.class);

        return ResponseEntity.ok(response.getBody());
    }

    private String buildUrl(String host, String path, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(host);
        sb.append(path);
        sb.append("?");

        queryParams.forEach((key, value) -> {
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("&");
        });

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", rapidApiKey);
        headers.set("x-rapidapi-host", RAPID_API_HOST);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        return headers;
    }

    private HttpEntity<?> buildRequestEntity(HttpHeaders headers) {
        return new HttpEntity<>(headers);
    }

}
