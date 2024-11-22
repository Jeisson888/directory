package com.jenesano.directory.controller;

import com.jenesano.directory.dto.EventDTO;
import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.entity.Event;
import com.jenesano.directory.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Obtener todos los eventos",
            description = "Devuelve una lista con todos los eventos registrados. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Obtener eventos actuales",
            description = "Devuelve una lista de los eventos que están actualmente activos.")
    @GetMapping("/current")
    public ResponseEntity<List<Event>> getCurrentEvents() {
        List<Event> events = eventService.getCurrentEvents();
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Obtener evento por ID",
            description = "Devuelve la información de un evento específico a partir de su ID.")
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Crear un nuevo evento",
            description = "Crea un nuevo evento en la base de datos. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = eventService.createEvent(eventDTO);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Actualizar un evento",
            description = "Actualiza los datos de un evento existente. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO eventDTO) {
        Event event = eventService.updateEvent(eventId, eventDTO);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Eliminar un evento",
            description = "Elimina un evento específico de la base de datos. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar imagen a un evento",
            description = "Asocia una imagen al evento especificado. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{eventId}/images")
    public ResponseEntity<Event> addImage(@PathVariable Long eventId, @RequestBody ImageDTO imageDTO) {
        Event event = eventService.addImage(eventId, imageDTO);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Eliminar imagen de un evento",
            description = "Elimina una imagen asociada a un evento específico. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{eventId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(@PathVariable Long eventId, @PathVariable Long imageId) {
        eventService.removeImage(eventId, imageId);
        return ResponseEntity.noContent().build();
    }
}
