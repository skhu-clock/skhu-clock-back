package com.example.skhuclock.domain.Restaurant;

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
@Schema(description = "식당")
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "식당 id")
    private Long id;


    @Schema(description = "식당까지의 거리")
    private int distance;

    @Schema(description = "카테고리")
    private String categoryName;


    @Schema(description = "식당 url")
    private String placeUrl;

    @Column(unique = true)
    @Schema(description = "식당 이름")
    private String name;

    @Schema(description = "식당 주소")
    private String addressName;


}
