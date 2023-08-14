package com.example.skhuclock.domain.Subway;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "지하철")
public class Subway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "지하철 호선ID")
    private Long subwayId;

    @Schema(description = "상하행선 구분")
    private String updnLine;

    @Schema(description = "도착지 방면")
    private String trainLineNm;

    @Schema(description = "첫번째 도착 메세지")
    private String arvlMsg1;

    @Schema(description = "두번째 도착 메세지")
    private String arvlMsg2;

    @Schema(description = "도착 코드")
    private int arvlCd;
}
