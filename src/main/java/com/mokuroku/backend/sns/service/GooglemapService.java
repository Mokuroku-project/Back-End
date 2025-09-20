package com.mokuroku.backend.sns.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GooglemapService {

    @Value("${google.maps.api-key}")
    private String mapApikey;

    // 외부 api를 호출하기 위한 HTTP 클라이언트
    private final RestTemplate restTemplate = new RestTemplate();

    // json 파싱
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
    위도와 경도를 받아서 google Maps API를 호출하고,
    해당 위치의 주소를 문자열로 반환
     */
    public JsonObject getAddressFromLatlLng(double latitude, double longitude) {
        try {
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&language=ko&key=%s"
                    , latitude, longitude, mapApikey
            );
            // API 호출 -> JSON 문자열 응답 받기
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Gson을 사용해 JSON 문자열을 JsonObject로 파싱
            return JsonParser.parseString(response.getBody()).getAsJsonObject();

        }  catch (Exception e) {
            // 예외 발생 시 빈 JsonObject 반환(또는 null 반환도 가능)
            return new JsonObject(); // 또는 null
        }
    }
}
