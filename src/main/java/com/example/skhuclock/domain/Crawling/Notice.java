package com.example.skhuclock.domain.Crawling;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "크롤링")
public class Notice {

    @Id
    @Schema(description = "공지사항 번호")
    private String articleNum;

    @Schema(description = "중요공지 일반공지")
    private String number;

    @Schema(description = "공지사항 상태")
    private String status;

    @Schema(description = "공지사항 제목")
    private String title;

    @Schema(description = "공지사항 작성일")
    private String writeDate;

    @Schema(description = "공지사항 작성자")
    private String author;

    @Schema(description = "공지사항 조회수")
    private int views;

    @Schema(description = "공지사항 url")
    private String url;
}
