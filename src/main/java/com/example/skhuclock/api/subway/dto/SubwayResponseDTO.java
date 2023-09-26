package com.example.skhuclock.api.subway.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SubwayResponseDTO {
    private List<SubwayRequestDto> subway;
    private String updnLine;
    private String subwayId;
}
