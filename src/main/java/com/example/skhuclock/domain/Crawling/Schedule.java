package com.example.skhuclock.domain.Crawling;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "학사일정 크롤링")
public class Schedule {

    @Id
    @Schema(description = "기간")
    private String date;

    @Schema(description = "일정 내용")
    private String txt;
}