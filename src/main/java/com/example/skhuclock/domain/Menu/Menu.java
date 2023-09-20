package com.example.skhuclock.domain.Menu;

import com.example.skhuclock.domain.Restaurant.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메뉴")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "메뉴 id")
    private Long id;

    @Column
    @Schema(description = "메뉴 이름")
    private String name;

    @Column
    @Schema(description = "메뉴 가격")
    private Long price;

    // 밥풀 and 1000원식당
    @Column
    @Schema(description = "메뉴 날짜")
    private String date;

    @Schema(description = "식당")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "restaurant_id")
    private Restaurant restaurant;

}
