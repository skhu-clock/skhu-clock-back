package com.example.skhuclock.domain.Crawling;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    //articleNum
    @Id
    private String articleNum;

    private String number;

    //상태
    private String status;

    //공지사항 제목
    private String title;

    //작성일
    private String writeDate;

    //작성자
    private String author;

    //조회수
    private int views;

    //url
    private String url;

}
