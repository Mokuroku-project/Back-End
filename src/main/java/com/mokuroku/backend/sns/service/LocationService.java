package com.mokuroku.backend.sns.service;

import com.mokuroku.backend.sns.entity.LocationEntity;
import com.mokuroku.backend.sns.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final GooglemapService googlemapService;

    /*
    위도,경도를 받아서 기존 Location이 있으면 재사용하고
    없으면 주소를 변환해서 새 Location을 생성한 뒤 저장
     */
    public LocationEntity findOrCreate(Double latitude, Double longitude){
        return locationRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> {
                    // google maps api 전체 응답 json 받아옴
                    JsonObject json = googlemapService.getAddressFromLatlLng(latitude, longitude);

                    // 첫 번째 결과 추출(가장 정확한 주소 정보)
                    JsonObject result = json.getAsJsonArray("results").get(0).getAsJsonObject();

                    // 전체 주소 문자열 추출
                    String formattedAddress = result.get("formatted_address").getAsString();

                    // 주소 구성 요소 배열 추출(시/구/동 등 세부 정보포함)
                    JsonArray components = result.getAsJsonArray("address_components");

                    // 새 Location 생성
                    LocationEntity location = new LocationEntity();
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    location.setAddress(formattedAddress); // 전체주소 저장

                    // address_components 파싱해서 city/district/neighborhood 분리해서 저장
                    for(JsonElement comp : components){
                        JsonObject obj = comp.getAsJsonObject();
                        String longName = obj.get("long_name").getAsString(); // 예:서울특별시
                        JsonArray types = obj.getAsJsonArray("types"); // 예: ["administrative_area_level_1", "political"]

                        // 시(city): administrative_area_level_1
                        if (containsType(types, "administrative_area_level_1")) {
                            location.setCity(longName);
                        }
                        // 구(district): sublocality_level_1
                        else if (containsType(types, "sublocality_level_1") || containsType(types, "sublocality")) {
                            location.setDistrict(longName);
                        }
                        // 동(neighborhood): neighborhood
                        else if (containsType(types, "neighborhood") || containsType(types, "premise")) {
                            location.setNeighborhood(longName);
                        }
                    }

                    return locationRepository.save(location);
                });
    }

    private boolean containsType(JsonArray types, String targetType){
        for (JsonElement type : types){
            if (type.getAsString().equals(targetType)){
                return true;
            }
        }
        return false;
    }
}
