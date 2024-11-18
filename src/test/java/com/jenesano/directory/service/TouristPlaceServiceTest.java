package com.jenesano.directory.service;

import com.jenesano.directory.dto.ImageDTO;
import com.jenesano.directory.dto.LocationDTO;
import com.jenesano.directory.dto.TouristPlaceDTO;
import com.jenesano.directory.entity.*;
import com.jenesano.directory.repository.TouristPlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TouristPlaceServiceTest {

    @Mock
    private TouristPlaceRepository touristPlaceRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private TouristPlaceService touristPlaceService;

    private TouristPlace touristPlace;
    private TouristPlaceDTO touristPlaceDTO;
    private ImageDTO imageDTO;
    private LocationDTO locationDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        touristPlace = new TouristPlace("Plaza Principal", "Un lugar icónico");
        touristPlace.setId(1L);
        touristPlace.setImages(new ArrayList<>());
        touristPlace.setLocation(new Location(5.1234, -72.5678));

        touristPlaceDTO = new TouristPlaceDTO();
        touristPlaceDTO.setName("Parque Central");
        touristPlaceDTO.setDescription("Hermoso parque para visitar");

        imageDTO = new ImageDTO();
        imageDTO.setUrl("https://example.com/image.jpg");

        locationDTO = new LocationDTO();
        locationDTO.setLatitude(6.1234);
        locationDTO.setLongitude(-71.5678);
    }

    @Test
    public void testGetAllTouristPlaces() {
        when(touristPlaceRepository.findAll()).thenReturn(List.of(touristPlace));

        List<TouristPlace> places = touristPlaceService.getAllTouristPlaces();

        assertNotNull(places);
        assertEquals(1, places.size());
        assertEquals("Plaza Principal", places.getFirst().getName());
        verify(reportService, times(1)).recordTouristPlaceVisit();
    }

    @Test
    public void testCreateTouristPlace() {
        when(touristPlaceRepository.save(ArgumentMatchers.any(TouristPlace.class))).thenReturn(touristPlace);

        TouristPlace createdPlace = touristPlaceService.createTouristPlace(touristPlaceDTO);

        assertNotNull(createdPlace);
        assertEquals("Plaza Principal", createdPlace.getName());
        assertEquals("Un lugar icónico", createdPlace.getDescription());
    }

    @Test
    public void testUpdateTouristPlace() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(touristPlace));
        when(touristPlaceRepository.save(touristPlace)).thenReturn(touristPlace);

        TouristPlace updatedPlace = touristPlaceService.updateTouristPlace(1L, touristPlaceDTO);

        assertNotNull(updatedPlace);
        assertEquals("Parque Central", updatedPlace.getName());
        assertEquals("Hermoso parque para visitar", updatedPlace.getDescription());
    }

    @Test
    public void testDeleteTouristPlace() {
        when(touristPlaceRepository.existsById(1L)).thenReturn(true);

        touristPlaceService.deleteTouristPlace(1L);

        verify(touristPlaceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddImage() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(touristPlace));
        when(touristPlaceRepository.save(touristPlace)).thenReturn(touristPlace);

        TouristPlace updatedPlace = touristPlaceService.addImage(1L, imageDTO);

        assertNotNull(updatedPlace);
        assertEquals(1, updatedPlace.getImages().size());
        assertEquals("https://example.com/image.jpg", updatedPlace.getImages().get(0).getUrl());
    }

    @Test
    public void testSetLocation() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(touristPlace));
        when(touristPlaceRepository.save(touristPlace)).thenReturn(touristPlace);

        TouristPlace updatedPlace = touristPlaceService.setLocation(1L, locationDTO);

        assertNotNull(updatedPlace);
        assertEquals(6.1234, updatedPlace.getLocation().getLatitude());
        assertEquals(-71.5678, updatedPlace.getLocation().getLongitude());
    }
}
