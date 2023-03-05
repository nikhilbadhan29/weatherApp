package com.example.weatherApp.controllers;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherController {
	private final String RAPID_API_HOST = "forecast9.p.rapidapi.com/rapidapi/forecast/";
	private final String RAPID_API_FORECAST_SUMMARY_PATH = "/summary/";
	private final String RAPID_API_HOURLY_FORECAST_PATH = "/hourly/";

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
	public String getForecastSummary(@RequestParam String city, @RequestHeader HttpHeaders headers,
			RestTemplate restTemplate) {
		if (validateCredentials(headers).getBody().equals("OK")) {

			Map<String, String> queryParams = new HashMap<>();
			queryParams.put("q", city);

			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpGet request = new HttpGet(buildUrl(RAPID_API_HOST, RAPID_API_FORECAST_SUMMARY_PATH, queryParams));
			request.addHeader("x-rapidapi-key", rapidApiKey);
			request.addHeader("x-rapidapi-host", RAPID_API_HOST);
			request.addHeader("Content-Type", "application/json");

			try {
				HttpResponse response = httpClient.execute(request);
				String json = EntityUtils.toString(response.getEntity());
				System.out.println(json.toString());

				return json.toString();
				// MyResponse myResponse = objectMapper.readValue(json, MyResponse.class);
				// String cityName = objectMapper.readValue(json,{city}).asText();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "No Data Found";
		} else {
			return "Unable to authenticate the user. Please check Client ID and client secret";
		}

	}

	@GetMapping("/hourly-forecast")
	public String getHourlyForecast(@RequestParam String city, @RequestHeader HttpHeaders headers,
			RestTemplate restTemplate) {
		if (validateCredentials(headers).getBody().equals("OK")) {
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put("q", city);

			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpGet request = new HttpGet(buildUrl(RAPID_API_HOST, RAPID_API_HOURLY_FORECAST_PATH, queryParams));
			request.addHeader("x-rapidapi-key", rapidApiKey);
			request.addHeader("x-rapidapi-host", RAPID_API_HOST);
			request.addHeader("Content-Type", "application/json");

			try {
				HttpResponse response = httpClient.execute(request);
				String json = EntityUtils.toString(response.getEntity());
				System.out.println(json.toString());
				ObjectMapper objectMapper = new ObjectMapper();
				return json.toString();
				// MyResponse myResponse = objectMapper.readValue(json, MyResponse.class);
				// String cityName = objectMapper.readValue(json,{city}).asText();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "No Data Found";
		} else {
			return "Unable to authenticate the user. Please check Client ID and client secret";
		}
	}

	private String buildUrl(String host, String path, Map<String, String> queryParams) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://");
		sb.append(host);
		queryParams.forEach((key, value) -> {
			sb.append(value);
		});

		sb.append(path);

		System.out.println("URL: " + sb);

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

	private ResponseEntity<String> validateCredentials(HttpHeaders headers) {
		if (headers.get("client-id") != null && headers.get("client-secret") != null
				&& headers.get("client-id").contains(clientId) && headers.get("client-secret").contains(clientSecret)) {
			System.out.println("clientId: "+clientId);
			System.out.println("clientSecret: "+clientSecret);
			return ResponseEntity.ok("OK");
		} else {
			return ResponseEntity.ok("Not Authenticated");
		}
	}

	private HttpEntity<?> buildRequestEntity(HttpHeaders headers) {
		return new HttpEntity<>(headers);
	}

}
