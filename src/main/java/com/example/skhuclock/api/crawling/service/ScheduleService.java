package com.example.skhuclock.api.crawling.service;

import com.example.skhuclock.domain.Crawling.Schedule;
import com.example.skhuclock.domain.Crawling.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final String BASE_URL = "https://www.skhu.ac.kr";
    private final String URL = BASE_URL + "/skhu/4166/subview.do";

    @PostConstruct
    public List<Schedule> getScheduleData() throws IOException {
        List<Schedule> schedules = new ArrayList<>();

        //Jsoup 연결
        Document document = Jsoup.connect(URL).get();
        // 각 학사일정마다 데이터 추출
        Elements scheList = document.select(".scheList li");

        for (Element sche : scheList) {

            // 날짜 추출
            String date = sche.select("dt span").first().text();

            // 내용 추출
            String txt = sche.select("dd span").first().text();

            Schedule schedule = Schedule.builder()
                    .date(date)
                    .txt(txt)
                    .build();
            schedules.add(schedule);
        }
        return scheduleRepository.saveAll(schedules);
    }
}