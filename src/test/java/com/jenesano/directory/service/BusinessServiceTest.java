package com.jenesano.directory.service;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.*;
import com.jenesano.directory.repository.BusinessRepository;
import com.jenesano.directory.repository.TypeBusinessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private TypeBusinessRepository typeBusinessRepository;

    @Mock
    private UserService userService;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private BusinessService businessService;

    private Business business;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        business = new Business(
                "Negocio",
                "123456789",
                "123456789",
                LocalDate.now(),
                "Jeisson Prieto",
                "Direccion",
                "3204080624",
                "https://www.negocio.com",
                "Descripcion del negocio",
                false,
                Status.ENABLED,
                new TypeBusiness("RESTAURANTE"),
                null
        );
    }

    @Test
    void testGetEnabledBusinesses() {
        List<Business> mockBusinesses = List.of(new Business());
        when(businessRepository.findByValidatedAndStatus(true, Status.ENABLED)).thenReturn(mockBusinesses);

        List<Business> result = businessService.getEnabledBusinesses();

        assertEquals(mockBusinesses, result);
        verify(reportService).recordBusinessVisit(null);
        verify(businessRepository).findByValidatedAndStatus(true, Status.ENABLED);
    }

    @Test
    void testGetAllBusinessesByTypeBusiness() {
        Long typeBusinessId = 1L;
        List<Business> mockBusinesses = List.of(new Business());
        when(businessRepository.findByValidatedAndStatusAndTypeBusinessId(true, Status.ENABLED, typeBusinessId))
                .thenReturn(mockBusinesses);

        List<Business> result = businessService.getAllBusinessesByTypeBusiness(typeBusinessId);

        assertEquals(mockBusinesses, result);
        verify(reportService).recordBusinessVisit(typeBusinessId);
        verify(businessRepository).findByValidatedAndStatusAndTypeBusinessId(true, Status.ENABLED, typeBusinessId);
    }

    @Test
    void testCreateBusiness() {
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setTypeBusinessId(1L);
        businessDTO.setName("Business Name");
        businessDTO.setRut("1234567890");
        businessDTO.setCommercialRegistration("CR123456");
        businessDTO.setRegistrationDate(LocalDate.now());
        businessDTO.setLegalRepresentative("John Doe");
        businessDTO.setAddress("123 Business Street");
        businessDTO.setPhoneNumber("987654321");
        businessDTO.setWebsite("https://www.businesswebsite.com");
        businessDTO.setDescription("Description of the business.");
        businessDTO.setStatus(Status.ENABLED);
        businessDTO.setEmail("business@example.com");
        TypeBusiness mockTypeBusiness = new TypeBusiness();
        User mockUser = new User();
        when(typeBusinessRepository.findById(1L)).thenReturn(Optional.of(mockTypeBusiness));
        when(userService.createOwnerUser(anyString())).thenReturn(mockUser);
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.createBusiness(businessDTO);

        assertNotNull(result);
        assertEquals("Business Name", result.getName());
        verify(businessRepository).save(any(Business.class));
    }

    @Test
    void testUpdateBusiness() {
        Long businessId = 1L;
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setTypeBusinessId(1L);
        businessDTO.setName("Updated Name");
        businessDTO.setRut("1234567890");
        businessDTO.setCommercialRegistration("CR123456");
        businessDTO.setRegistrationDate(LocalDate.now());
        businessDTO.setLegalRepresentative("John Doe");
        businessDTO.setAddress("123 Business Street");
        businessDTO.setPhoneNumber("987654321");
        businessDTO.setWebsite("https://www.businesswebsite.com");
        businessDTO.setDescription("Description of the business.");
        businessDTO.setStatus(Status.ENABLED);
        businessDTO.setEmail("business@example.com");
        Business mockBusiness = business;
        TypeBusiness mockTypeBusiness = new TypeBusiness();
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(typeBusinessRepository.findById(1L)).thenReturn(Optional.of(mockTypeBusiness));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.updateBusiness(businessId, businessDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(businessRepository).save(mockBusiness);
    }

    @Test
    void testGetAllTypesBusiness() {
        List<TypeBusiness> mockTypes = List.of(new TypeBusiness());
        when(typeBusinessRepository.findAll()).thenReturn(mockTypes);

        List<TypeBusiness> result = businessService.getAllTypesBusiness();

        assertEquals(mockTypes, result);
        verify(typeBusinessRepository).findAll();
    }

    @Test
    void testSetLocation() {
        Long businessId = 1L;
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(10.0);
        locationDTO.setLongitude(20.0);
        Business mockBusiness = new Business();
        mockBusiness.setLocation(new Location());
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.setLocation(businessId, locationDTO);

        assertNotNull(result);
        assertEquals(10.0, result.getLocation().getLatitude());
        assertEquals(20.0, result.getLocation().getLongitude());
        verify(businessRepository).save(mockBusiness);
    }

    @Test
    void testSetBusinessHours() {
        Long businessId = 1L;

        BusinessHourDTO businessHourDTO = new BusinessHourDTO();
        businessHourDTO.setDayWeek(DayWeek.MONDAY);
        businessHourDTO.setOpeningTime(LocalTime.of(9, 0));
        businessHourDTO.setClosingTime(LocalTime.of(17, 0));

        List<BusinessHourDTO> businessHourDTOs = List.of(businessHourDTO);

        Business mockBusiness = business;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.setBusinessHours(businessId, businessHourDTOs);

        assertNotNull(result);
        verify(businessRepository).save(mockBusiness);
    }

    @Test
    void testAddImage() {
        Long businessId = 1L;
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl("http://example.com/image.jpg");
        Business mockBusiness = business;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.addImage(businessId, imageDTO);

        assertNotNull(result);
        assertEquals(1, result.getImages().size());
        verify(businessRepository).save(mockBusiness);
    }

    @Test
    void testAddBusinessContent() {
        Long businessId = 1L;
        BusinessContentDTO contentDTO = new BusinessContentDTO();
        contentDTO.setName("Content Name");
        contentDTO.setDescription("Description");
        contentDTO.setPrice(10000);
        contentDTO.setImageUrl("http://example.com/image.jpg");
        Business mockBusiness = business;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.addBusinessContent(businessId, contentDTO);

        assertNotNull(result);
        assertEquals(1, result.getBusinessContents().size());
        verify(businessRepository).save(mockBusiness);
    }

    @Test
    void testAddReview() {
        Long businessId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReview(5);
        reviewDTO.setUserId(1L);
        User mockUser = new User();
        Business mockBusiness = business;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(userService.getUserById(1L)).thenReturn(mockUser);
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business result = businessService.addReview(businessId, reviewDTO);

        assertNotNull(result);
        assertEquals(1, result.getReviews().size());
        verify(businessRepository).save(mockBusiness);
    }
}
