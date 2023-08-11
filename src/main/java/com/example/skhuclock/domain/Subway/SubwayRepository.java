package com.example.skhuclock.domain.Subway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayRepository extends JpaRepository<Subway,Long> {

}