package com.example.skhuclock.api.subway.controller;

import com.example.skhuclock.api.subway.dto.SubwayResponseDTO;
import com.example.skhuclock.api.subway.service.SubwayService;
import com.example.skhuclock.domain.Subway.Subway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/subway")
@Slf4j
@Tag(name = "Subway", description = "지하철 API Document")
public class SubwayController {
    private final SubwayService subwayService;

    @GetMapping
    @Operation(summary = "subway json", description = "지하철 정보 출력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Subway.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<List<SubwayResponseDTO>> getSubway() {
        try {
            List<SubwayResponseDTO> dto = subwayService.getSubway();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error while fetching subway information", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}