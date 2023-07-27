package com.example.skhuclock.domain.Weather;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class Weather {

    private Long temp;

    private Long rainAmount;

    private Long humid;

    private String lastUpdateTime;
}
