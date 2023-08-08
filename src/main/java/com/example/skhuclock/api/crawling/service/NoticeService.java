package com.example.skhuclock.api.crawling.service;



import com.example.skhuclock.domain.Crawling.NoticeRepository;
import com.example.skhuclock.domain.Crawling.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final String BASE_URL = "https://www.skhu.ac.kr";
    private final String URL = BASE_URL + "/skhu/1038/subview.do";

    @PostConstruct
    public List<Notice> getNoticeData() throws IOException {
        List<Notice> notices = new ArrayList<>();

        //Jsoup 연결
        Document document = Jsoup.connect(URL).get();

        // 각 공지사항마다 데이터 추출
        Elements contents = document.select("table.board-table.horizon1 tbody tr.notice");

        for (Element content : contents) {

            // 번호 추출 (공지사항의 번호는 td.td-num 안에 있는 span 클래스의 텍스트로 추출)
            String number = content.select("td.td-num span").text();

            // 상태 추출 (공지사항의 상태는 td.td-state 안에 있는 텍스트로 추출)
            String status = content.select("td.td-state").text();
            // "진행중"인 경우에만 상태를 "진행중"으로 설정, 그 외에는 빈 문자열로 설정
            if ("진행중".equals(status)) {
                status = "진행중";
            } else {
                status = "";
            }

            // 제목 추출 (공지사항의 제목은 td.td-subject 안에 있는 strong 태그의 텍스트로 추출)
            String title = content.select("td.td-subject strong").text();

            // 작성일 추출 (공지사항의 작성일은 td.td-date 안에 있는 텍스트로 추출)
            String writeDate = content.select("td.td-date").text();

            // 작성자 추출 (공지사항의 작성자는 td.td-write 안에 있는 텍스트로 추출)
            String author = content.select("td.td-write").text();

            // 조회수 추출 (공지사항의 조회수는 td.td-access 안에 있는 텍스트를 정수로 변환하여 추출)
            int views = Integer.parseInt(content.select("td.td-access").text());

            // 공지사항 url 추출
            Element linkElement = content.select("a").first();
            String relativeUrl = linkElement.attr("href");

            // BASE_URL(기본 https) + relativeUrl
            String fullUrl = BASE_URL + relativeUrl;


            // articleNum 공지사항 number 가져오기
            String href = linkElement.attr("href");
            String[] parts = href.split("/");
            String articleNum = parts[parts.length - 2];

            // Notice 객체 생성 후 리스트에 추가
            Notice notice = Notice.builder()
                    .number(number)
                    .status(status)
                    .title(title)
                    .writeDate(writeDate)
                    .author(author)
                    .views(views)
                    .url(fullUrl)
                    .articleNum(articleNum)
                    .build();
            notices.add(notice);
        }
        return noticeRepository.saveAll(notices);
    }
}