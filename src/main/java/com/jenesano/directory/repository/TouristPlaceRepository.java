package com.jenesano.directory.repository;

import com.jenesano.directory.entity.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long> {
}
