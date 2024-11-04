package com.jenesano.directory.service;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.*;
import com.jenesano.directory.exception.BusinessAlreadyValidatedException;
import com.jenesano.directory.exception.EntityNotFoundException;
import com.jenesano.directory.repository.BusinessRepository;
import com.jenesano.directory.repository.TypeBusinessRepository;
import com.jenesano.directory.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final TypeBusinessRepository typeBusinessRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Autowired
    public BusinessService(BusinessRepository businessRepository, TypeBusinessRepository typeBusinessRepository, UserService userService, PasswordEncoder passwordEncoder, EmailService emailService, UserRepository userRepository) {
        this.businessRepository = businessRepository;
        this.typeBusinessRepository = typeBusinessRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    public List<Business> getEnabledBusinesses() {
        return businessRepository.findByValidatedAndStatus(true, Status.ENABLED);
    }

    public List<Business> getDisabledBusinesses() {
        return businessRepository.findByValidatedAndStatus(true, Status.DISABLED);
    }

    public List<Business> getNonValidatedBusinesses() {
        return businessRepository.findByValidated(false);
    }

    public List<Business> getAllBusinessesByTypeBusiness(Long typeBusinessId) {
        return businessRepository.findByValidatedAndStatusAndTypeBusinessId(true, Status.ENABLED, typeBusinessId);
    }

    public List<Business> getAllBusinessesByReview(int review) {
        return businessRepository.findByValidatedAndStatusAndReviewsReview(true, Status.ENABLED, review);
    }

    public Business getBusinessById(Long businessId) {
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Negocio", businessId));
    }

    public Business createBusiness(BusinessDTO businessDTO) {
        TypeBusiness typeBusiness = getTypeBusinessById(businessDTO.getTypeBusinessId());

        validateBusiness(businessDTO);
        Business business = new Business(
                businessDTO.getName(),
                businessDTO.getRut(),
                businessDTO.getCommercialRegistration(),
                businessDTO.getRegistrationDate(),
                businessDTO.getLegalRepresentative(),
                businessDTO.getAddress(),
                businessDTO.getPhoneNumber(),
                businessDTO.getWebsite(),
                businessDTO.getDescription(),
                false,
                businessDTO.getStatus(),
                typeBusiness,
                userService.createOwnerUser(businessDTO.getEmail())
        );

        return businessRepository.save(business);
    }

    public Business updateBusiness(Long businessId, BusinessDTO businessDTO) {
        Business business = getBusinessById(businessId);
        TypeBusiness typeBusiness = getTypeBusinessById(businessDTO.getTypeBusinessId());

        validateBusiness(businessDTO);
        business.setName(businessDTO.getName());
        business.setRut(businessDTO.getRut());
        business.setCommercialRegistration(businessDTO.getCommercialRegistration());
        business.setRegistrationDate(businessDTO.getRegistrationDate());
        business.setLegalRepresentative(businessDTO.getLegalRepresentative());
        business.setAddress(businessDTO.getAddress());
        business.setPhoneNumber(businessDTO.getPhoneNumber());
        business.setWebsite(businessDTO.getWebsite());
        business.setDescription(businessDTO.getDescription());
        business.setStatus(businessDTO.getStatus());
        business.setTypeBusiness(typeBusiness);

        return businessRepository.save(business);
    }

    private void validateBusiness(BusinessDTO businessDTO) {
        if (businessDTO.getName() == null || businessDTO.getName().isEmpty()
                || businessDTO.getRut() == null || businessDTO.getRut().isEmpty()
                || businessDTO.getLegalRepresentative() == null || businessDTO.getLegalRepresentative().isEmpty()
                || businessDTO.getStatus() == null) {
            throw new IllegalArgumentException("El nombre, rut, representante legal y estado del negocio no pueden ser nulos o vacios.");
        }
    }

    public void deleteBusiness(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new EntityNotFoundException("Negocio", businessId);
        }
        businessRepository.deleteById(businessId);
    }

    public void validateBusiness(Long businessId) {
        Business business = getBusinessById(businessId);

        if (business.isValidated()) {
            throw new BusinessAlreadyValidatedException(businessId);
        }
        business.setValidated(true);
        User user = business.getUser();
        String randomPassword = RandomStringUtils.randomAlphanumeric(5);
        user.setEncryptedPassword(passwordEncoder.encode(randomPassword));
        user.setStatus(Status.ENABLED);

        businessRepository.save(business);
        userRepository.save(user);
        emailService.sendCredentialsToEmail(user.getEmail(), user.getUsername(), randomPassword);
    }

    public List<TypeBusiness> getAllTypesBusiness() {
        return this.typeBusinessRepository.findAll();
    }

    public TypeBusiness getTypeBusinessById(Long typeBusinessId) {
        return typeBusinessRepository.findById(typeBusinessId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de negocio", typeBusinessId));
    }

    public TypeBusiness createTypeBusiness(TypeBusinessDTO typeBusinessDTO) {
        validateTypeBusiness(typeBusinessDTO);
        TypeBusiness typeBusiness = new TypeBusiness(typeBusinessDTO.getName());

        return typeBusinessRepository.save(typeBusiness);
    }

    public TypeBusiness updateTypeBusiness(Long typeBusinessId, TypeBusinessDTO typeBusinessDTO) {
        TypeBusiness typeBusiness = getTypeBusinessById(typeBusinessId);

        validateTypeBusiness(typeBusinessDTO);
        typeBusiness.setName(typeBusinessDTO.getName());

        return typeBusinessRepository.save(typeBusiness);
    }

    private void validateTypeBusiness(TypeBusinessDTO typeBusinessDTO) {
        if (typeBusinessDTO.getName() == null || typeBusinessDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de negocio no puede ser nulo o vacio.");
        }
    }

    public void deleteTypeBusiness(Long typeBusinessId) {
        if (!typeBusinessRepository.existsById(typeBusinessId)) {
            throw new EntityNotFoundException("Tipo de negocio", typeBusinessId);
        }
        typeBusinessRepository.deleteById(typeBusinessId);
    }

    public Business setLocation(Long businessId, LocationDTO locationDTO) {
        Business business = getBusinessById(businessId);

        if (locationDTO.getLatitude() == null || locationDTO.getLongitude() == null) {
            throw new IllegalArgumentException("La latitud y longitud de la localizacion no pueden ser nulas.");
        }
        business.getLocation().setLatitude(locationDTO.getLatitude());
        business.getLocation().setLongitude(locationDTO.getLongitude());

        return businessRepository.save(business);
    }

    public Business setBusinessHours(Long businessId, List<BusinessHourDTO> businessHoursDTO) {
        Business business = getBusinessById(businessId);

        for (BusinessHourDTO businessHourDTO : businessHoursDTO) {
            if (businessHourDTO.getDayWeek() == null
                    || businessHourDTO.getOpeningTime() == null
                    || businessHourDTO.getClosingTime() == null) {
                throw new IllegalArgumentException("El dia de la semana, hora de apertura y hora de cierre del horario de negocio no pueden ser nulos.");
            }
        }
        List<BusinessHour> businessHours = businessHoursDTO.stream()
                .map(businessHourDTO -> new BusinessHour(
                        businessHourDTO.getDayWeek(),
                        businessHourDTO.getOpeningTime(),
                        businessHourDTO.getClosingTime()))
                .toList();
        business.getBusinessHours().clear();
        business.getBusinessHours().addAll(businessHours);

        return businessRepository.save(business);
    }

    public Business addImage(Long businessId, ImageDTO imageDTO) {
        Business business = getBusinessById(businessId);

        if (imageDTO.getUrl() == null || imageDTO.getUrl().isEmpty()) {
            throw new IllegalArgumentException("La url de la imagen no puede ser nula o vacia.");
        }
        Image image = new Image(imageDTO.getUrl());
        business.getImages().add(image);

        return businessRepository.save(business);
    }

    public void removeImage(Long businessId, Long imageId) {
        Business business = getBusinessById(businessId);

        boolean removed = business.getImages().removeIf(image -> image.getId().equals(imageId));
        if (!removed) {
            throw new EntityNotFoundException("Imagen", imageId);
        }

        businessRepository.save(business);
    }

    public Business addBusinessContent(Long businessId, BusinessContentDTO businessContentDTO) {
        Business business = getBusinessById(businessId);

        validateBusinessContent(businessContentDTO);
        BusinessContent businessContent = new BusinessContent(
                businessContentDTO.getName(),
                businessContentDTO.getDescription(),
                businessContentDTO.getPrice(),
                new Image(businessContentDTO.getImageUrl())
        );
        business.getBusinessContents().add(businessContent);

        return businessRepository.save(business);
    }

    public Business updateBusinessContent(Long businessId, Long businessContentId, BusinessContentDTO businessContentDTO) {
        Business business = getBusinessById(businessId);

        validateBusinessContent(businessContentDTO);
        BusinessContent businessContentToUpdate = business.getBusinessContents().stream()
                .filter(businessContent -> businessContent.getId().equals(businessContentId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Contenido de negocio", businessContentId));
        businessContentToUpdate.setName(businessContentDTO.getName());
        businessContentToUpdate.setDescription(businessContentDTO.getDescription());
        businessContentToUpdate.setPrice(businessContentDTO.getPrice());
        businessContentToUpdate.setImage(new Image(businessContentDTO.getImageUrl()));

        return businessRepository.save(business);
    }

    private void validateBusinessContent(BusinessContentDTO businessContentDTO) {
        if (businessContentDTO.getName() == null || businessContentDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del contenido de negocio no puede ser nulo o vacio.");
        }
    }

    public void removeBusinessContent(Long businessId, Long businessContentId) {
        Business business = getBusinessById(businessId);

        boolean removed = business.getBusinessContents().removeIf(businessContent -> businessContent.getId().equals(businessContentId));
        if (!removed) {
            throw new EntityNotFoundException("Contenido de negocio", businessContentId);
        }

        businessRepository.save(business);
    }

    public Business addReview(Long businessId, ReviewDTO reviewDTO) {
        Business business = getBusinessById(businessId);

        // validar
        Review review = new Review(
                reviewDTO.getReview(),
                reviewDTO.getDescription(),
                LocalDate.now(),
                TypeReview.BUSINESS,
                userService.getUserById(reviewDTO.getUserId())
        );
        business.getReviews().add(review);

        return  businessRepository.save(business);
    }

    public Business updateReview(Long businessId, Long reviewId, ReviewDTO reviewDTO) {
        Business business = getBusinessById(businessId);

        // validar
        Review reviewToUpdate = business.getReviews().stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Reseña", reviewId));
        reviewToUpdate.setReview(reviewDTO.getReview());
        reviewToUpdate.setDescription(reviewDTO.getDescription());

        return  businessRepository.save(business);
    }

    public void removeReview(Long businessId, Long reviewId) {
        Business business = getBusinessById(businessId);

        boolean removed = business.getReviews().removeIf(review -> review.getId().equals(reviewId));
        if (!removed) {
            throw new EntityNotFoundException("Reseña", reviewId);
        }

        businessRepository.save(business);
    }
}
