package com.jenesano.directory.service;

import com.jenesano.directory.entity.Event;
import com.jenesano.directory.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return null;
    }

    public Optional<Event> getEventById(Long id) {
        return Optional.empty();
    }

    public Event createEvent(Event event) {
        return null;
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        return null;
    }

    public void deleteEvent(Long id) {

    }
}
