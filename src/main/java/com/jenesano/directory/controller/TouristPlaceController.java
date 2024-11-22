package com.jenesano.directory.controller;

import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.dto.LocationDTO;
import com.jenesano.directory.dto.TouristPlaceDTO;
import com.jenesano.directory.entity.TouristPlace;
import com.jenesano.directory.service.TouristPlaceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tourist-places")
public class TouristPlaceController {

    private final TouristPlaceService touristPlaceService;

    @Autowired
    public TouristPlaceController(TouristPlaceService touristPlaceService) {
        this.touristPlaceService = touristPlaceService;
    }

    @Operation(summary = "Obtener todos los lugares turísticos",
            description = "Obtiene todos los lugares turísticos disponibles en el sistema.")
    @GetMapping
    public ResponseEntity<List<TouristPlace>> getAllTouristPlaces() {
        List<TouristPlace> touristPlaces = touristPlaceService.getAllTouristPlaces();
        return ResponseEntity.ok(touristPlaces);
    }

    @Operation(summary = "Obtener un lugar turístico por ID",
            description = "Obtiene un lugar turístico específico mediante su ID.")
    @GetMapping("/{touristPlaceId}")
    public ResponseEntity<TouristPlace> getTouristPlaceById(@PathVariable Long touristPlaceId) {
        TouristPlace touristPlace = touristPlaceService.getTouristPlaceById(touristPlaceId);
        return ResponseEntity.ok(touristPlace);
    }

    @Operation(summary = "Crear un nuevo lugar turístico",
            description = "Permite a los usuarios con roles ADMIN o MANAGER crear un nuevo lugar turístico.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<TouristPlace> createTouristPlace(@RequestBody TouristPlaceDTO touristPlaceDTO) {
        TouristPlace touristPlace = touristPlaceService.createTouristPlace(touristPlaceDTO);
        return new ResponseEntity<>(touristPlace, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un lugar turístico",
            description = "Permite a los usuarios con roles ADMIN o MANAGER actualizar un lugar turístico existente.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{touristPlaceId}")
    public ResponseEntity<TouristPlace> updateTouristPlace(@PathVariable Long touristPlaceId, @RequestBody TouristPlaceDTO touristPlaceDTO) {
        TouristPlace touristPlace = touristPlaceService.updateTouristPlace(touristPlaceId, touristPlaceDTO);
        return ResponseEntity.ok(touristPlace);
    }

    @Operation(summary = "Eliminar un lugar turístico",
            description = "Permite a los usuarios con roles ADMIN o MANAGER eliminar un lugar turístico específico.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{touristPlaceId}")
    public ResponseEntity<Void> deleteTouristPlace(@PathVariable Long touristPlaceId) {
        touristPlaceService.deleteTouristPlace(touristPlaceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Añadir una imagen a un lugar turístico",
            description = "Permite a los usuarios con roles ADMIN o MANAGER añadir una imagen a un lugar turístico.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{touristPlaceId}/images")
    public ResponseEntity<TouristPlace> addImage(@PathVariable Long touristPlaceId, @RequestBody ImageDTO imageDTO) {
        TouristPlace touristPlace = touristPlaceService.addImage(touristPlaceId, imageDTO);
        return ResponseEntity.ok(touristPlace);
    }

    @Operation(summary = "Eliminar una imagen de un lugar turístico",
            description = "Permite a los usuarios con roles ADMIN o MANAGER eliminar una imagen de un lugar turístico.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{touristPlaceId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(@PathVariable Long touristPlaceId, @PathVariable Long imageId) {
        touristPlaceService.removeImage(touristPlaceId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Establecer la ubicación de un lugar turístico",
            description = "Permite a los usuarios con roles ADMIN o MANAGER establecer la ubicación de un lugar turístico.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{touristPlaceId}/location")
    public ResponseEntity<TouristPlace> setLocation(@PathVariable Long touristPlaceId, @RequestBody LocationDTO locationDTO) {
        TouristPlace touristPlace = touristPlaceService.setLocation(touristPlaceId, locationDTO);
        return ResponseEntity.ok(touristPlace);
    }
}
