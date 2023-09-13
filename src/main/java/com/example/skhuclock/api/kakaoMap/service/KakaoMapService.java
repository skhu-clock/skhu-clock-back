package com.example.skhuclock.api.kakaoMap.service;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;
import com.example.skhuclock.api.weather.dto.WeatherResponseDTO;
import com.example.skhuclock.domain.Restaurant.Restaurant;
import com.example.skhuclock.domain.Restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoMapService {
    @Value("${kakaoKey}")
    private String kakaoKey;

    private final RestaurantRepository restaurantRepository;
    // url 만들어주는 메소드
    public URI  getUrl() {
        URI uri = UriComponentsBuilder
                // 음식점 카테고리 설정
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6")
                // 성공회대 근방 좌표
                .queryParam("x", 126.825205)
                .queryParam("y",37.487680)
                // 근방 800m 까지 찾기
                .queryParam("radius",800)
                .encode()
                .build()
                .toUri();

        return uri;
    }

    // 어플리케이션과 url 연결 (get)
    // 에러가 나거나 아닌 경우에 따라 값 출력
    public String ErrorApi(URI urlBuilder) throws IOException{
        URL url = new URL(urlBuilder.toString());
        log.info("request url: {}", url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader rd;
        try {

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Authorization", kakaoKey);

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return sb.toString();
        } catch (JSONException e){
            log.info(url.toString());
            log.info(((HttpURLConnection) url.openConnection()).getErrorStream().toString());
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            line = rd.readLine();
            sb.append(line);
            return sb.toString();
        }

    }

    @Transactional
    public List<KakaoMapResponseDTO> getRestaurant() throws IOException {
        List<KakaoMapResponseDTO> listDto = new ArrayList<>();

        URI urlBuilder = getUrl(); //공개된 Url
        try {
            String data = ErrorApi(urlBuilder);

            // 수정 해야 할 것 같은 코드 -> requestDto 생성
            ArrayList<String> placeNames = new ArrayList<>();
            ArrayList<String> categoryNames = new ArrayList<>();
            ArrayList<String> distances = new ArrayList<>();
            ArrayList<String> AddressNames = new ArrayList<>();

            JSONObject jObject = new JSONObject(data);
            JSONArray documents = jObject.getJSONArray("documents");


            // 식당 엔티티에 있는 정보들 저장
            for (int i = 0; i < documents.length(); i++) {
                JSONObject obj = documents.getJSONObject(i);

                String placeName = obj.getString("place_name");
                placeNames.add(placeName);

                String categoryName = obj.getString("category_name");
                categoryNames.add(categoryName);

                String distance = obj.getString("distance");
                distances.add(distance);

                String roadAddressName = obj.getString("address_name");
                AddressNames.add(roadAddressName);

            }
            // 필요한 내용 DTO로 저장
            for (int i = 0; i < 15; i++) {

                Restaurant restaurant = Restaurant.builder()
                        .name(placeNames.get(i))
                        .categoryName(categoryNames.get(i))
                        .distance(distances.get(i))
                        .addressName(AddressNames.get(i))
                        .build();

                restaurantRepository.save(restaurant);

                KakaoMapResponseDTO dto = KakaoMapResponseDTO.builder()
                        .restaurant(restaurant)
                        .message("OK").build();

                listDto.add(dto);
            }

            // 필요한 내용 DTO로 저장
            return listDto;


        } catch (IOException e) {
            // 오류 코드
            KakaoMapResponseDTO dto = KakaoMapResponseDTO.builder()
                    .restaurant(null)
                    .message("카카오맵 정보를 받아오는 오류가 생겼어여").build();
            listDto.add(dto);

            return listDto;
        }
    }

}
