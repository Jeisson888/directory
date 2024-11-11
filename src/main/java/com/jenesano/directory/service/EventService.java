package com.jenesano.directory.service;

import com.jenesano.directory.dto.EventDTO;
import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.entity.Event;
import com.jenesano.directory.entity.Image;
import com.jenesano.directory.exception.EntityNotFoundException;
import com.jenesano.directory.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ReportService reportService;

    @Autowired
    public EventService(EventRepository eventRepository, ReportService reportService) {
        this.eventRepository = eventRepository;
        this.reportService = reportService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getCurrentEvents() {
        reportService.recordEventVisit();
        return eventRepository.findByDateGreaterThanEqual(LocalDate.now());
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("evento", eventId));
    }

    public Event createEvent(EventDTO eventDTO) {
        validateEventDTO(eventDTO);
        Event event = new Event(
                eventDTO.getName(),
                eventDTO.getDescription(),
                eventDTO.getDate()
        );

        return eventRepository.save(event);
    }

    public Event updateEvent(Long eventId, EventDTO eventDTO) {
        Event event = getEventById(eventId);

        validateEventDTO(eventDTO);
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());

        return eventRepository.save(event);
    }

    private void validateEventDTO(EventDTO eventDTO) {
        if (eventDTO.getName() == null || eventDTO.getName().isEmpty() || eventDTO.getDate() == null) {
            throw new IllegalArgumentException("El nombre y fecha del evento no pueden ser nulos o vacios.");
        }
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("evento", eventId);
        }
        eventRepository.deleteById(eventId);
    }

    public Event addImage(Long eventId, ImageDTO imageDTO) {
        Event event = getEventById(eventId);

        if (imageDTO.getUrl() == null || imageDTO.getUrl().isEmpty()) {
            throw new IllegalArgumentException("La url de la imagen no puede ser nula o vacia.");
        }
        Image image = new Image(imageDTO.getUrl());
        event.getImages().add(image);

        return eventRepository.save(event);
    }

    public void removeImage(Long eventId, Long imageId) {
        Event event = getEventById(eventId);

        boolean removed = event.getImages().removeIf(image -> image.getId().equals(imageId));
        if (!removed) {
            throw new EntityNotFoundException("Imagen", imageId);
        }

        eventRepository.save(event);
    }
}
