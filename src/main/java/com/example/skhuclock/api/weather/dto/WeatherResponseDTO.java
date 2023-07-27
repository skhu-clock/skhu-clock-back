package com.example.skhuclock.api.weather.dto;

import com.example.skhuclock.domain.Weather.Weather;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WeatherResponseDTO {
    private Weather weather;
    private String message;
}


