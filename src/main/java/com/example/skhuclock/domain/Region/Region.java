package com.example.skhuclock.domain.Region;

import com.example.skhuclock.domain.Weather.Weather;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String region;
    @Column
    private int nx;
    @Column
    private int ny;

    @Embedded
    private Weather weather;

    @Builder
    public Region(Long id, String region, int nx, int ny) {
        this.id = id;
        this.region = region;
        this.nx = nx;
        this.ny = ny;
    }

    public void updateRegionWeather(Weather weather) {
        this.weather = weather;
    }

}
