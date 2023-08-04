package com.example.skhuclock.api.weather.controller;


import com.example.skhuclock.api.weather.dto.WeatherResponseDTO;
import com.example.skhuclock.api.weather.service.WeatherService;
import com.example.skhuclock.domain.Weather.Weather;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("api/weather")
@Slf4j
@Tag(name = "Weather", description = "날씨 API Document")
public class WeatherController {


    private final WeatherService weatherService;

    @GetMapping
    @Operation(summary = "weather json", description = "날씨 정보 출력", tags = {"View"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<?> getWeather() {
        List<WeatherResponseDTO> dto = weatherService.getWeather();
        return ResponseEntity.ok(dto);
    }
}


