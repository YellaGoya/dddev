package com.d103.alert;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/alert-service")
@Slf4j
public class AlertController {

	@Value("${git.personal-token}")
	private String token;

	@GetMapping("/add-alert")
	public ResponseEntity<?> addAlert() {
		String result = null;

		HashMap<String, String> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/vnd.github+json");
		headers.add("Authorization", "Bearer "+token);
		headers.add("X-GitHub-Api-Version", "2022-11-28");

		// body.put();

		HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(
			"https://api.github.com/repos/"+"gayun0303/webhook-test"+"/hooks",
			HttpMethod.GET,
			entity,
			String.class
		);

		log.info("response {}", response);
		log.info("body {}", response.getBody());



		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}

	@PostMapping("/receive-alert")
	public ResponseEntity<?> receiveAlert(@RequestHeader(required = false) Map<String, String> headerMap, @RequestBody(required = false) Map<String, String> bodyMap) {

		log.info("header value start==============");
		for(String key : headerMap.keySet()) {
			String value = headerMap.get(key);
			log.info("key: {}, value: {}", key, value);
		}
		log.info("body value start================");
		for(String key : bodyMap.keySet()) {
			String value = bodyMap.get(key);
			log.info("key: {}, value: {}", key, value);
		}

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
