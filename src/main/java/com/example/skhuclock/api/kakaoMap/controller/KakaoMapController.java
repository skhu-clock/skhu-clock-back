package com.example.skhuclock.api.kakaoMap.controller;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;
import com.example.skhuclock.api.kakaoMap.service.KakaoMapService;
import com.example.skhuclock.api.weather.dto.WeatherResponseDTO;
import com.example.skhuclock.domain.Restaurant.Restaurant;
import com.example.skhuclock.domain.Restaurant.RestaurantRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/KakaoMap")
@Slf4j
@Tag(name = "KakaoMap", description = "카카오맵 API Document")
public class KakaoMapController {

    private final KakaoMapService kakaoMapService;
    @PostConstruct
    public ResponseEntity<?> getRestaurant() throws IOException, ParseException {


        List<KakaoMapResponseDTO> restaurant = kakaoMapService.getRestaurant();

        return ResponseEntity.ok(restaurant);
    }
}
