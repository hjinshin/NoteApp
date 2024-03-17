package com.noteapp.ocr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MathPixService {
    @Value("${ocr_app_id}")
    private String app_id;
    @Value("${ocr_api_key}")
    private String app_key;

    private final String MATHPIX_API_URL = "https://api.mathpix.com/v3/text";

    public String recognizeMathExpression(String imgByte) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("app_id", app_id);
        headers.set("app_key", app_key);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("src", "data:image/jpeg;base64," + imgByte);

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity
                .post(new URI(MATHPIX_API_URL))
                .headers(headers)
                .body(requestBody);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseMap = responseEntity.getBody();
        return responseMap.get("latex_styled").toString();
    }
}
