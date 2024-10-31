package com.jenesano.directory.service;

import com.jenesano.directory.entity.TouristPlace;
import com.jenesano.directory.repository.TouristPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TouristPlaceService {

    private final TouristPlaceRepository touristPlaceRepository;

    @Autowired
    public TouristPlaceService(TouristPlaceRepository touristPlaceRepository) {
        this.touristPlaceRepository = touristPlaceRepository;
    }

    public List<TouristPlace> getAllTouristPlaces() {
        return null;
    }

    public Optional<TouristPlace> getTouristPlaceById(Long id) {
        return Optional.empty();
    }

    public TouristPlace createTouristPlace(TouristPlace touristPlace) {
        return null;
    }

    public TouristPlace updateTouristPlace(Long id, TouristPlace updatedTouristPlace) {
        return null;
    }

    public void deleteTouristPlace(Long id) {

    }
}
