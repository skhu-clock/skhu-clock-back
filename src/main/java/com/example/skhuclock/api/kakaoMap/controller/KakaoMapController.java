package com.example.skhuclock.api.kakaoMap.controller;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;
import com.example.skhuclock.api.kakaoMap.service.KakaoMapService;
import com.example.skhuclock.domain.Restaurant.Restaurant;
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
@RequestMapping("api/KakaoMap")
@Slf4j
@Tag(name = "KakaoMap", description = "카카오맵 API Document")
public class KakaoMapController {

    private final KakaoMapService kakaoMapService;

    @GetMapping
    @Operation(summary = "Restaurant json", description = "식당 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Restaurant.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<List<KakaoMapResponseDTO>> getRestaurant() {

        List<KakaoMapResponseDTO> dto = kakaoMapService.getRestaurant(1);
        dto.addAll(kakaoMapService.getRestaurant(2));
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/distance")
    @Operation(summary = "Restaurant json", description = "식당 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Restaurant.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<List<KakaoMapResponseDTO>> getRestaurant2() {

        return ResponseEntity.ok(kakaoMapService.findAll());

    }

}
