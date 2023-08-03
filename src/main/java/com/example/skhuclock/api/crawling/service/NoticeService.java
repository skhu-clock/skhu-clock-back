package com.example.skhuclock.api.crawling.service;


import com.example.skhuclock.domain.Crawling.NoticeRepository;
import com.example.skhuclock.domain.Crawling.Notice;
import lombok.RequiredArgsConstructor;
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
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final String URL = "https://www.skhu.ac.kr/skhu/1038/subview.do";

//    public String getNoticeUrl(int page) {
//        return URL + "?" + "pageIndex=" + page;
//    }
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

            // 상태 추출 (공지사항의 상태는 td.td-ing 안에 있는 btn-ing 클래스의 텍스트로 추출)
            String status = content.select("td.td-ing span.btn-ing").text();

            // 제목 추출 (공지사항의 제목은 td.td-subject 안에 있는 strong 태그의 텍스트로 추출)
            String title = content.select("td.td-subject strong").text();

            // 작성일 추출 (공지사항의 작성일은 td.td-date 안에 있는 텍스트로 추출)
            String writeDate = content.select("td.td-date").text();

            // 작성자 추출 (공지사항의 작성자는 td.td-write 안에 있는 텍스트로 추출)
            String author = content.select("td.td-write").text();

            // 조회수 추출 (공지사항의 조회수는 td.td-access 안에 있는 텍스트를 정수로 변환하여 추출)
            int views = Integer.parseInt(content.select("td.td-access").text());

            // 첨부파일 추출 (공지사항의 첨부파일은 td.td-file 안에 있는 파일 URL로 추출)
            String attachment = content.select("td.td-file a").attr("abs:href");

            // Notice 객체 생성 후 리스트에 추가
            Notice notice = Notice.builder()
                    .number(number)
                    .status(status)
                    .title(title)
                    .writeDate(writeDate)
                    .author(author)
                    .views(views)
                    .attachment(attachment)
                    .build();
            notices.add(notice);
        }
       return noticeRepository.saveAll(notices);
    }

}