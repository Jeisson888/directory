package com.jenesano.directory.service;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.*;
import com.jenesano.directory.exception.BusinessAlreadyValidatedException;
import com.jenesano.directory.exception.EntityNotFoundException;
import com.jenesano.directory.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private TypeBusinessRepository typeBusinessRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private BusinessService businessService;

    private Business business;

    @BeforeEach
    void setUp() {
        business = new Business(
                "Negocio",
                "123456789",
                "123456789",
                LocalDate.now(),
                "Jeisson Prieto",
                "Direccion",
                "3204080624",
                "www.negocio.com",
                "Descripcion",
                false,
                Status.ENABLED,
                new TypeBusiness("RESTAURANTE"),
                null
        );
    }

    @Test
    void testGetAllBusinesses() {
        when(businessRepository.findAll()).thenReturn(Collections.emptyList());

        List<Business> businesses = businessService.getAllBusinesses();

        assertTrue(businesses.isEmpty());
        verify(businessRepository, times(1)).findAll();
    }

    @Test
    void testGetEnabledBusinesses() {
        when(businessRepository.findByValidatedAndStatus(true, Status.ENABLED)).thenReturn(Collections.emptyList());

        List<Business> businesses = businessService.getEnabledBusinesses();

        assertTrue(businesses.isEmpty());
        verify(reportService, times(1)).recordBusinessVisit(null);
        verify(businessRepository, times(1)).findByValidatedAndStatus(true, Status.ENABLED);
    }

    @Test
    void testGetBusinessByIdThrowsExceptionWhenNotFound() {
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            businessService.getBusinessById(businessId);
        });

        assertEquals("Negocio con id 1 no encontrado.", thrown.getMessage());
        verify(businessRepository, times(1)).findById(businessId);
    }

    @Test
    void testCreateBusiness() {
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setName("Test Business");
        businessDTO.setTypeBusinessId(1L);
        businessDTO.setStatus(Status.ENABLED);
        businessDTO.setRut("12345678-9");
        businessDTO.setLegalRepresentative("John Doe");

        TypeBusiness typeBusiness = new TypeBusiness("Food");
        when(typeBusinessRepository.findById(businessDTO.getTypeBusinessId())).thenReturn(Optional.of(typeBusiness));
        when(businessRepository.save(any(Business.class))).thenReturn(new Business());

        Business createdBusiness = businessService.createBusiness(businessDTO);

        assertNotNull(createdBusiness);
        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    void testDeleteBusinessThrowsExceptionWhenNotFound() {
        Long businessId = 1L;
        when(businessRepository.existsById(businessId)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            businessService.deleteBusiness(businessId);
        });

        assertEquals("Negocio con id 1 no encontrado.", thrown.getMessage());
        verify(businessRepository, times(1)).existsById(businessId);
    }

    @Test
    void testValidateBusinessThrowsExceptionIfAlreadyValidated() {
        Long businessId = 1L;
        business.setValidated(true);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        BusinessAlreadyValidatedException thrown = assertThrows(BusinessAlreadyValidatedException.class, () -> {
            businessService.validateBusiness(businessId);
        });

        assertEquals("Negocio con id 1 ya esta validado.", thrown.getMessage());
        verify(businessRepository, times(1)).findById(businessId);
    }

    @Test
    void testGetAllBusinessesByTypeBusiness() {
        Long typeBusinessId = 1L;
        when(businessRepository.findByValidatedAndStatusAndTypeBusinessId(true, Status.ENABLED, typeBusinessId))
                .thenReturn(Collections.emptyList());

        List<Business> businesses = businessService.getAllBusinessesByTypeBusiness(typeBusinessId);

        assertTrue(businesses.isEmpty());
        verify(reportService, times(1)).recordBusinessVisit(typeBusinessId);
        verify(businessRepository, times(1)).findByValidatedAndStatusAndTypeBusinessId(true, Status.ENABLED, typeBusinessId);
    }

    @Test
    void testAddImage() {
        Long businessId = 1L;
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl("https://image-url.com");

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(businessRepository.save(business)).thenReturn(business);

        Business updatedBusiness = businessService.addImage(businessId, imageDTO);

        assertEquals(1, updatedBusiness.getImages().size());
        assertEquals("https://image-url.com", updatedBusiness.getImages().getFirst().getUrl());
        verify(businessRepository, times(1)).save(business);
    }

    @Test
    void testRemoveImageThrowsExceptionWhenNotFound() {
        Long businessId = 1L;
        Long imageId = 2L;

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            businessService.removeImage(businessId, imageId);
        });

        assertEquals("Imagen con id 2 no encontrado.", thrown.getMessage());
    }
}
