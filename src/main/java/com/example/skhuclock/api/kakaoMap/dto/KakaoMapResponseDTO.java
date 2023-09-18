package com.example.skhuclock.api.kakaoMap.dto;

import com.example.skhuclock.domain.Restaurant.Restaurant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoMapResponseDTO {
    private Restaurant restaurant;
    private String message;
}


