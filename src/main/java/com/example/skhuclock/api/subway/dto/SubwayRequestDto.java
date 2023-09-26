package com.example.skhuclock.api.subway.dto;

import lombok.Builder;

import lombok.Getter;


@Getter
public class SubwayRequestDto {
    private Long subwayId;
    private String updnLine;
    private String trainLineNm;
    private String arvlMsg1;
    private String arvlMsg2;
    private int arvlCd;

    @Builder
    public SubwayRequestDto(Long subwayId, String updnLine, String trainLineNm, String arvlMsg1, String arvlMsg2, int arvlCd) {
        this.subwayId = subwayId;
        this.updnLine = updnLine;
        this.trainLineNm = trainLineNm;
        this.arvlMsg1 = arvlMsg1;
        this.arvlMsg2 = arvlMsg2;
        this.arvlCd = arvlCd;
    }

    @Builder
    public SubwayRequestDto(SubwayRequestDto subwayRequestDto) {
        this.subwayId = subwayRequestDto.getSubwayId();
        this.trainLineNm = subwayRequestDto.getTrainLineNm();
        this.arvlMsg1 = subwayRequestDto.getArvlMsg1();
        this.arvlMsg2 = subwayRequestDto.getArvlMsg2();
        this.arvlCd = subwayRequestDto.getArvlCd();
    }
}
