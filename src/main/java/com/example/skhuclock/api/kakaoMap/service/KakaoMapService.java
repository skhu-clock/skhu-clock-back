package com.example.skhuclock.api.kakaoMap.service;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;
import com.example.skhuclock.api.kakaoMap.dto.RestaurantRequestDTO;
import com.example.skhuclock.domain.Restaurant.Restaurant;
import com.example.skhuclock.domain.Restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoMapService {
    @Value("${kakaoKey}")
    private String kakaoKey;

    private final RestaurantRepository restaurantRepository;
    // url 만들어주는 메소드
    public URI getUrl(Integer page) {
        URI uri = UriComponentsBuilder
                // 음식점 카테고리 설정
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6")
                // 성공회대 근방 좌표
                .queryParam("x", 126.825205)
                .queryParam("y", 37.487680)
                .queryParam("page", page)
                // 근방 800m 까지 찾기
                .queryParam("radius", 800)
                .encode()
                .build()
                .toUri();

        return uri;
    }

    // 어플리케이션과 url 연결 (get)
    // 에러가 나거나 아닌 경우에 따라 값 출력
    public String ErrorApi(URI urlBuilder) throws IOException {
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
        } catch (JSONException e) {
            log.info(url.toString());
            log.info(((HttpURLConnection) url.openConnection()).getErrorStream().toString());
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            line = rd.readLine();
            sb.append(line);
            return sb.toString();
        }

    }

    @Transactional(readOnly = true)
    public List<KakaoMapResponseDTO> getRestaurant(Integer page) {
        List<KakaoMapResponseDTO> listDto = new ArrayList<>();
        List<RestaurantRequestDTO> restaurants = new ArrayList<>();
        URI urlBuilder = getUrl(page); //공개된 Url
        try {
            String data = ErrorApi(urlBuilder);


            JSONObject jObject = new JSONObject(data);
            JSONArray documents = jObject.getJSONArray("documents");


            // 식당 엔티티에 있는 정보들 저장
            for (int i = 0; i < documents.length(); i++) {
                JSONObject obj = documents.getJSONObject(i);
                RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                        .AddressName(obj.getString("address_name"))
                        .categoryName(obj.getString("category_name"))
                        .distance(obj.getInt("distance"))
                        .placeName(obj.getString("place_name"))
                        .place_url(obj.getString("place_url"))
                        .build();
                restaurants.add(requestDTO);

            }
            // 필요한 내용 DTO로 저장
            for (int i = 0; i < 15; i++) {
                RestaurantRequestDTO requestDTO = restaurants.get(i);

                Restaurant restaurant = Restaurant.builder()
                        .name(requestDTO.getPlaceName())
                        .categoryName(requestDTO.getCategoryName())
                        .distance(requestDTO.getDistance())
                        .addressName(requestDTO.getAddressName())
                        .placeUrl(requestDTO.getPlace_url())
                        .build();
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
                    .message("카카오맵 정보를 받아오는 데 오류가 생겼어여").build();
            listDto.add(dto);

            return listDto;
        }
    }

    @Transactional(readOnly = true)
    public List<KakaoMapResponseDTO> findAll(){
        return restaurantRepository.findAll2().orElseThrow();
    }


}
