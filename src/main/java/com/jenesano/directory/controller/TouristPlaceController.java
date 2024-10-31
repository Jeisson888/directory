package com.jenesano.directory.controller;

import com.jenesano.directory.entity.TouristPlace;
import com.jenesano.directory.service.TouristPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<TouristPlace>> getAllTouristPlaces() {
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TouristPlace>> getTouristPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok().body(Optional.empty());
    }

    @PostMapping
    public ResponseEntity<TouristPlace> createTouristPlace(@RequestBody TouristPlace touristPlace) {
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TouristPlace> updateTouristPlace(@PathVariable Long id, @RequestBody TouristPlace touristPlace) {
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTouristPlace(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
