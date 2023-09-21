package com.example.skhuclock.domain.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RestaurantRepository  extends JpaRepository<Restaurant,Long>,RestaurantRepositoryCustom {
    Restaurant findByName(String name);
}
