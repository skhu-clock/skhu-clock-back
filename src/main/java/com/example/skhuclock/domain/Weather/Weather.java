package com.example.skhuclock.domain.Weather;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
@Schema(description = "날씨 관련 임베디드")
public class Weather {

    @Schema(description = "온도")
    private String temp;

    @Schema(description = "강수확률")
    private String precipitation;

    @Schema(description = "습도")
    private String humid;

    @Schema(description = "발표시각")
    private String baseTime;

    @Schema(description = "예보시각")
    private String forecastTime;

}
