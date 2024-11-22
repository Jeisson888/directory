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

@Service
public class TouristPlaceService {

    private final TouristPlaceRepository touristPlaceRepository;
    private final ReportService reportService;

    @Autowired
    public TouristPlaceService(TouristPlaceRepository touristPlaceRepository, ReportService reportService) {
        this.touristPlaceRepository = touristPlaceRepository;
        this.reportService = reportService;
    }

    // Registra la visita al sitio turístico y devuelve la lista de todos los sitios turísticos.
    public List<TouristPlace> getAllTouristPlaces() {
        reportService.recordTouristPlaceVisit();
        return touristPlaceRepository.findAll();
    }

    // Obtiene un sitio turístico por su ID. Si no existe, lanza una excepción.
    public TouristPlace getTouristPlaceById(Long touristPlaceId) {
        return touristPlaceRepository.findById(touristPlaceId)
                .orElseThrow(() -> new EntityNotFoundException("Sitios turistico", touristPlaceId));
    }

    // Crea un nuevo sitio turístico usando los datos proporcionados en el DTO.
    public TouristPlace createTouristPlace(TouristPlaceDTO touristPlaceDTO) {
        validateTouristPlaceDTO(touristPlaceDTO);
        TouristPlace touristPlace = new TouristPlace(
                touristPlaceDTO.getName(),
                touristPlaceDTO.getDescription()
        );

        return touristPlaceRepository.save(touristPlace);
    }

    // Actualiza un sitio turístico existente con los nuevos datos del DTO.
    public TouristPlace updateTouristPlace(Long touristPlaceId, TouristPlaceDTO touristPlaceDTO) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        validateTouristPlaceDTO(touristPlaceDTO);
        touristPlace.setName(touristPlaceDTO.getName());
        touristPlace.setDescription(touristPlaceDTO.getDescription());

        return touristPlaceRepository.save(touristPlace);
    }

    // Valida que el nombre del sitio turístico no sea nulo o vacío.
    private void validateTouristPlaceDTO(TouristPlaceDTO touristPlaceDTO) {
        if (touristPlaceDTO.getName() == null || touristPlaceDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del sitio turistico no puede ser nulo o vacio.");
        }
    }

    // Elimina un sitio turístico por su ID. Lanza excepción si no se encuentra.
    public void deleteTouristPlace(Long touristPlaceId) {
        if (!touristPlaceRepository.existsById(touristPlaceId)) {
            throw new EntityNotFoundException("Sitios turistico", touristPlaceId);
        }
        touristPlaceRepository.deleteById(touristPlaceId);
    }

    // Añade una imagen al sitio turístico. Si la URL de la imagen es inválida, lanza una excepción.
    public TouristPlace addImage(Long touristPlaceId, ImageDTO imageDTO) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        if (imageDTO.getUrl() == null || imageDTO.getUrl().isEmpty()) {
            throw new IllegalArgumentException("La url de la imagen no puede ser nula o vacia.");
        }
        Image image = new Image(imageDTO.getUrl());
        touristPlace.getImages().add(image);

        return touristPlaceRepository.save(touristPlace);
    }

    // Elimina una imagen del sitio turístico por su ID. Lanza excepción si no se encuentra la imagen.
    public void removeImage(Long touristPlaceId, Long imageId) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        boolean removed = touristPlace.getImages().removeIf(image -> image.getId().equals(imageId));
        if (!removed) {
            throw new EntityNotFoundException("Imagen", imageId);
        }

        touristPlaceRepository.save(touristPlace);
    }

    // Establece la ubicación del sitio turístico con las coordenadas proporcionadas en el DTO.
    public TouristPlace setLocation(Long touristPlaceId, LocationDTO locationDTO) {
        TouristPlace touristPlace = getTouristPlaceById(touristPlaceId);

        touristPlace.getLocation().setLatitude(locationDTO.getLatitude());
        touristPlace.getLocation().setLongitude(locationDTO.getLongitude());

        return touristPlaceRepository.save(touristPlace);
    }
}
