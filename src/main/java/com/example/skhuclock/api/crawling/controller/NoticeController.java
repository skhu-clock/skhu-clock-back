package com.example.skhuclock.api.crawling.controller;


import com.example.skhuclock.domain.Crawling.Notice;
import com.example.skhuclock.api.crawling.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/crawling")
public class NoticeController {

    private final NoticeService noticeService;

    // 크롤링한 공지사항 정보 전체 조회
    @GetMapping("/notices")
    public ResponseEntity<Notice> getAllNotices() throws IOException {
        return new ResponseEntity(noticeService.getNoticeData(),
                HttpStatus.OK);
    }
}