package com.example.skhuclock.domain.Region;

import com.example.skhuclock.domain.Weather.Weather;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지역 관련 엔티티")
public class Region {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "지역 id")
    private Long id;

    @Column
    @Schema(description = "지역")
    private String region;
    @Column
    @Schema(description = "예보지점 X 좌표")
    private int nx;
    @Column
    @Schema(description = "예보지점 Y 좌표")
    private int ny;

    @Embedded
    private Weather weather;





}
