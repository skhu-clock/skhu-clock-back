package com.example.skhuclock.api.kakaoMap.service;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;
import com.example.skhuclock.domain.Restaurant.Restaurant;
import com.example.skhuclock.domain.Restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@EnableScheduling
@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoScheduledService {

    private final KakaoMapService kakaoMapService;

    private final long fixedDelayTime = 1000L * 60 * 60 * 24 * 30;      //30일
    private final RestaurantRepository restaurantRepository;

    @Scheduled(fixedDelay = fixedDelayTime, initialDelay = 1000)
    @Transactional
    public void saveRestaurant() {
        List<KakaoMapResponseDTO> responseDtos = kakaoMapService.getRestaurant(1);
        responseDtos.addAll(kakaoMapService.getRestaurant(2));
        for (int i = 0; i < 30; i++) {
            KakaoMapResponseDTO kakaoMapResponseDTO = responseDtos.get(i);

            Restaurant restaurant = Restaurant.builder()
                    .id((long) i + 1)
                    .addressName(kakaoMapResponseDTO.getRestaurant().getAddressName())
                    .distance(kakaoMapResponseDTO.getRestaurant().getDistance())
                    .categoryName(kakaoMapResponseDTO.getRestaurant().getCategoryName())
                    .name(kakaoMapResponseDTO.getRestaurant().getName())
                    .placeUrl(kakaoMapResponseDTO.getRestaurant().getPlaceUrl())
                    .build();
            restaurantRepository.save(restaurant);
        }


    }
}
