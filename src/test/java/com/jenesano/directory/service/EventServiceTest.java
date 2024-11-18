package com.jenesano.directory.service;

import com.jenesano.directory.dto.EventDTO;
import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.entity.Event;
import com.jenesano.directory.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventDTO eventDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        event = new Event("Event", "Description", LocalDate.now());
        event.setId(1L);

        eventDTO = new EventDTO();
        eventDTO.setName("Event");
        eventDTO.setDescription("Description");
        eventDTO.setDate(LocalDate.now());
    }

    @Test
    public void testGetCurrentEvents() {
        when(eventRepository.findByDateGreaterThanEqual(LocalDate.now())).thenReturn(List.of(event));

        List<Event> currentEvents = eventService.getCurrentEvents();

        assertNotNull(currentEvents);
        assertEquals(1, currentEvents.size());
        assertEquals("Event", currentEvents.getFirst().getName());
    }

    @Test
    public void testGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.getEventById(1L);

        assertNotNull(foundEvent);
        assertEquals("Event", foundEvent.getName());
    }

    @Test
    public void testCreateEvent() {
        when(eventRepository.save(ArgumentMatchers.any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(eventDTO);

        assertNotNull(createdEvent);
        assertEquals("Event", createdEvent.getName());
    }

    @Test
    public void testUpdateEvent() {
        EventDTO updatedEventDTO = new EventDTO();
        updatedEventDTO.setName("Updated Event");
        updatedEventDTO.setDescription("Updated Description");
        updatedEventDTO.setDate(LocalDate.now().plusDays(1));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(ArgumentMatchers.any(Event.class))).thenReturn(event);

        Event updatedEvent = eventService.updateEvent(1L, updatedEventDTO);

        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getName());
        assertEquals("Updated Description", updatedEvent.getDescription());
    }

    @Test
    public void testDeleteEvent() {
        when(eventRepository.existsById(1L)).thenReturn(true);

        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddImage() {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl("http://example.com/image.jpg");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(ArgumentMatchers.any(Event.class))).thenReturn(event);

        Event updatedEvent = eventService.addImage(1L, imageDTO);

        assertNotNull(updatedEvent);
        assertEquals(1, updatedEvent.getImages().size());
        assertEquals("http://example.com/image.jpg", updatedEvent.getImages().get(0).getUrl());
    }
}
