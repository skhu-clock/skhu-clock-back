package com.example.skhuclock.api.kakaoMap.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestaurantRequestDTO {
    private Long id;
    private String placeName;
    private String categoryName;
    private int distance;
    private String AddressName;
    private String place_url;

}
