package com.example.skhuclock.domain.Restaurant;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryCustom {
    Optional<List<KakaoMapResponseDTO>> findAll2();
}
