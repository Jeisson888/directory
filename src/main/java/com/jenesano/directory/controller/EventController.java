package com.jenesano.directory.controller;

import com.jenesano.directory.dto.EventDTO;
import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.entity.Event;
import com.jenesano.directory.service.EventService;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/current")
    public ResponseEntity<List<Event>> getCurrentEvents() {
        List<Event> events = eventService.getCurrentEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = eventService.createEvent(eventDTO);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO eventDTO) {
        Event event = eventService.updateEvent(eventId, eventDTO);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{eventId}/images")
    public ResponseEntity<Event> addImage(@PathVariable Long eventId, @RequestBody ImageDTO imageDTO) {
        Event event = eventService.addImage(eventId, imageDTO);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{eventId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(@PathVariable Long eventId, @PathVariable Long imageId) {
        eventService.removeImage(eventId, imageId);
        return ResponseEntity.noContent().build();
    }
}
