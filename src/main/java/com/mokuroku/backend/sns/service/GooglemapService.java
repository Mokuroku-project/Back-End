package com.mokuroku.backend.sns.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GooglemapService {

    @Value("${google.maps.api-key}")
    private String mapApikey;

    // 외부 api를 호출하기 위한 HTTP 클라이언트
    private final WebClient.Builder webClientBuilder;

    /*
    위도와 경도를 받아서 google Maps API를 호출하고,
    해당 위치의 주소를 문자열로 반환
     */
    public JsonObject getAddressFromLatlLng(double latitude, double longitude) {
        try {
            WebClient client = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api").build();
//
            Mono<String> responseMono = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geocode/json")
                            .queryParam("latlng", latitude + "," + longitude)
                            .queryParam("language", "ko")
                            .queryParam("key", mapApikey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = responseMono.block(); //여기서 응답 꺼냄

            // Gson을 사용해 JSON 문자열을 JsonObject로 파싱
            return JsonParser.parseString(responseBody).getAsJsonObject();

        }  catch (Exception e) {
            // 예외 발생 시 빈 JsonObject 반환(또는 null 반환도 가능)
            return new JsonObject(); // 또는 null
        }
    }
}
