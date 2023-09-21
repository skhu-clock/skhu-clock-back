package com.example.skhuclock.domain.Restaurant;

import com.example.skhuclock.api.kakaoMap.dto.KakaoMapResponseDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.skhuclock.domain.Restaurant.QRestaurant.restaurant;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryCustomImpl implements RestaurantRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<KakaoMapResponseDTO>> findAll2() {
        List<KakaoMapResponseDTO> result= new ArrayList<>();
        List<Restaurant> resDto = jpaQueryFactory
                .select(Projections.fields(Restaurant.class,restaurant.id,restaurant.distance,restaurant.categoryName,restaurant.placeUrl,restaurant.name,restaurant.addressName))
                .from(restaurant)
                .orderBy(restaurant.distance.asc())
                .fetch();

        for (Restaurant tuple : resDto) {
            KakaoMapResponseDTO responseDTOS = KakaoMapResponseDTO.builder()
                    .restaurant(tuple)
                    .message("OK")
                    .build();
            result.add(responseDTOS);
        }
        return Optional.ofNullable(result);
    }

}
