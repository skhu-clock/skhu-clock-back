package com.example.skhuclock.api.crawling.controller;

import com.example.skhuclock.api.crawling.service.ScheduleService;
import com.example.skhuclock.domain.Crawling.Schedule;
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
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/schedule")
@Slf4j
@Tag(name = "Schedule", description = "학사일정 크롤링 API Document")
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 크롤링한 학사일정 정보 전체 조회
    @GetMapping
    @Operation(summary = "schedule crawling json", description = "학사일정 크롤링 정보 출력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Schedule.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<Schedule> getAllSchedules() throws IOException {
        return new ResponseEntity(scheduleService.getScheduleData(),
                HttpStatus.OK);
    }
}