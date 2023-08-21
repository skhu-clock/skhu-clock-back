package com.example.skhuclock.api.crawling.controller;

import com.example.skhuclock.domain.Crawling.Notice;
import com.example.skhuclock.api.crawling.service.NoticeService;
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
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/notice")
@Slf4j
@Tag(name = "Notice", description = "학사공지 크롤링 API Document")
public class NoticeController {
    private final NoticeService noticeService;

    // 크롤링한 공지사항 정보 전체 조회
    @GetMapping
    @Operation(summary = "notice crawling json", description = "학사공지 크롤링 정보 출력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Notice.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<Notice> getAllNotices() throws IOException {
        return new ResponseEntity(noticeService.getNoticeData(),
                HttpStatus.OK);
    }
}