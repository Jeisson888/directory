package com.jenesano.directory.service;

import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.dto.LocationDTO;
import com.jenesano.directory.dto.TouristPlaceDTO;
import com.jenesano.directory.entity.*;
import com.jenesano.directory.exception.EntityNotFoundException;
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
        return touristPlaceRepository.findAll();
    }

    public TouristPlace getTouristPlaceById(Long touristPlaceId) {
        return touristPlaceRepository.findById(touristPlaceId)
                .orElseThrow(() -> new EntityNotFoundException("Sitios turistico", touristPlaceId));
    }

    public TouristPlace createTouristPlace(TouristPlaceDTO touristPlaceDTO) {
        validateTouristPlaceDTO(touristPlaceDTO);
        TouristPlace touristPlace = new TouristPlace(
               touristPlaceDTO.getName(),
               touristPlaceDTO.getDescription()
        );

        return touristPlaceRepository.save(touristPlace);
    }

    public TouristPlace updateTouristPlace(Long touristPlaceId, TouristPlaceDTO touristPlaceDTO) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        validateTouristPlaceDTO(touristPlaceDTO);
        touristPlace.setName(touristPlaceDTO.getName());
        touristPlace.setDescription(touristPlaceDTO.getDescription());

        return touristPlaceRepository.save(touristPlace);
    }

    private void validateTouristPlaceDTO(TouristPlaceDTO touristPlaceDTO) {
        if (touristPlaceDTO.getName() == null || touristPlaceDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del sitio turistico no puede ser nulo o vacio.");
        }
    }

    public void deleteTouristPlace(Long touristPlaceId) {
        if (!touristPlaceRepository.existsById(touristPlaceId)) {
            throw new EntityNotFoundException("Sitios turistico", touristPlaceId);
        }
        touristPlaceRepository.deleteById(touristPlaceId);
    }

    public TouristPlace addImage(Long touristPlaceId, ImageDTO imageDTO) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        if (imageDTO.getUrl() == null || imageDTO.getUrl().isEmpty()) {
            throw new IllegalArgumentException("La url de la imagen no puede ser nula o vacia.");
        }
        Image image = new Image(imageDTO.getUrl());
        touristPlace.getImages().add(image);

        return touristPlaceRepository.save(touristPlace);
    }

    public void removeImage(Long touristPlaceId, Long imageId) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        boolean removed = touristPlace.getImages().removeIf(image -> image.getId().equals(imageId));
        if (!removed) {
            throw new EntityNotFoundException("Imagen", imageId);
        }

        touristPlaceRepository.save(touristPlace);
    }

    public TouristPlace setLocation(Long touristPlaceId, LocationDTO locationDTO) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        touristPlace.getLocation().setLatitude(locationDTO.getLatitude());
        touristPlace.getLocation().setLongitude(locationDTO.getLongitude());

        return touristPlaceRepository.save(touristPlace);
    }
}
