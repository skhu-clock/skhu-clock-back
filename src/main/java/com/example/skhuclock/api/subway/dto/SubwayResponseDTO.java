package com.example.skhuclock.api.subway.dto;

import com.example.skhuclock.domain.Subway.Subway;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubwayResponseDTO {
    private Subway subway;
}
