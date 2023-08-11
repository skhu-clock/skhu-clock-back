package com.example.skhuclock.domain.Subway;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "지하철")
public class Subway {


    @Id
    @Schema(description = "지하철호선ID")
    private int subwayId;

    @Schema(description = "상하행선구분")
    private String updnLine;

    @Schema(description = "도착지방면")
    private String trainLineNm;

    @Schema(description = "지하철역명")
    private String statnNm;

    @Schema(description = "첫번째 도착메세지")
    private String arvlMsg2;

    @Schema(description = "첫번째 도착메세지")
    private String arvlMsg3;

    @Schema(description = "도착코드")
    private int arvlCd;

}
