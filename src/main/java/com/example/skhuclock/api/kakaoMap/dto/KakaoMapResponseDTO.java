package com.example.skhuclock.api.kakaoMap.dto;

import com.example.skhuclock.domain.Restaurant.Restaurant;
import com.example.skhuclock.domain.Weather.Weather;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoMapResponseDTO {
    private Restaurant restaurant;
    private String message;
}


