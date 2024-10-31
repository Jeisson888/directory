package com.jenesano.directory.controller;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.Business;
import com.jenesano.directory.entity.TypeBusiness;
import com.jenesano.directory.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Business>> getAllBusinesses() {
        List<Business> businesses = businessService.getAllBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/validated-enabled")
    public ResponseEntity<List<Business>> getValidatedEnabledBusinesses() {
        List<Business> businesses = businessService.getValidatedEnabledBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/validated-disabled")
    public ResponseEntity<List<Business>> getValidatedDisableBusinesses() {
        List<Business> businesses = businessService.getValidatedDisabledBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/non-validated")
    public ResponseEntity<List<Business>> getNonValidatedBusinesses() {
        List<Business> businesses = businessService.getNonValidatedBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/type/{typeBusinessId}")
    public ResponseEntity<List<Business>> getAllBusinessesByTypeBusiness(@PathVariable Long typeBusinessId) {
        List<Business> businesses = businessService.getAllBusinessesByTypeBusiness(typeBusinessId);
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/reviews/{review}")
    public ResponseEntity<List<Business>> getAllBusinessesByReview(@PathVariable int review) {
        List<Business> businesses = businessService.getAllBusinessesByReview(review);
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<Business> getBusinessById(@PathVariable Long businessId) {
        Business business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

    @PostMapping
    public ResponseEntity<Business> createBusiness(@RequestBody BusinessDTO businessDTO) {
        Business business = businessService.createBusiness(businessDTO);
        return new ResponseEntity<>(business, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PutMapping("/{businessId}")
    public ResponseEntity<Business> updateBusiness(@PathVariable Long businessId, @RequestBody BusinessDTO businessDTO) {
        Business business = businessService.updateBusiness(businessId, businessDTO);
        return ResponseEntity.ok(business);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long businessId) {
        businessService.deleteBusiness(businessId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{businessId}/validate")
    public ResponseEntity<Void> validateBusiness(@PathVariable Long businessId) {
        businessService.validateBusiness(businessId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<TypeBusiness>> getAllTypesBusinesses() {
        List<TypeBusiness> typesBusiness = businessService.getAllTypesBusiness();
        return ResponseEntity.ok(typesBusiness);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/types/{businessId}")
    public ResponseEntity<TypeBusiness> getTypeBusinessById(@PathVariable Long businessId) {
        TypeBusiness typeBusiness = businessService.getTypeBusinessById(businessId);
        return ResponseEntity.ok(typeBusiness);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/types")
    public ResponseEntity<TypeBusiness> createTypeBusiness(@RequestBody TypeBusinessDTO typeBusinessDTO) {
        TypeBusiness typeBusiness = businessService.createTypeBusiness(typeBusinessDTO);
        return new ResponseEntity<>(typeBusiness, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/types/{businessId}")
    public ResponseEntity<TypeBusiness> updateTypeBusiness(@PathVariable Long businessId, @RequestBody TypeBusinessDTO typeBusinessDTO) {
        TypeBusiness typeBusiness = businessService.updateTypeBusiness(businessId, typeBusinessDTO);
        return ResponseEntity.ok(typeBusiness);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/types/{businessId}")
    public ResponseEntity<Void> deleteTypeBusiness(@PathVariable Long businessId) {
        businessService.deleteTypeBusiness(businessId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/location")
    public ResponseEntity<Business> setLocation(@PathVariable Long businessId, @RequestBody LocationDTO locationDTO) {
        Business business = businessService.setLocation(businessId, locationDTO);
        return ResponseEntity.ok(business);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/hours")
    public ResponseEntity<Business> setBusinessHours(@PathVariable Long businessId, @RequestBody List<BusinessHourDTO> businessHoursDTO) {
        Business business = businessService.setBusinessHours(businessId, businessHoursDTO);
        return ResponseEntity.ok(business);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/images")
    public ResponseEntity<Business> addImage(@PathVariable Long businessId, @RequestBody ImageDTO imageDTO) {
        Business business = businessService.addImage(businessId, imageDTO);
        return ResponseEntity.ok(business);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @DeleteMapping("/{businessId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(@PathVariable Long businessId, @PathVariable Long imageId) {
        businessService.removeImage(businessId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/contents")
    public ResponseEntity<Business> addBusinessContent(@PathVariable Long businessId, @RequestBody BusinessContentDTO businessContentDTO) {
        Business business = businessService.addBusinessContent(businessId, businessContentDTO);
        return ResponseEntity.ok(business);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PutMapping("/{businessId}/contents/{businessContentId}")
    public ResponseEntity<Business> updateBusinessContent(@PathVariable Long businessId, @PathVariable Long businessContentId, @RequestBody BusinessContentDTO businessContentDTO) {
        Business business = businessService.updateBusinessContent(businessId, businessContentId, businessContentDTO);
        return ResponseEntity.ok(business);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @DeleteMapping("/{businessId}/contents/{businessContentId}")
    public ResponseEntity<Void> removeBusinessContent(@PathVariable Long businessId, @PathVariable Long businessContentId) {
        businessService.removeBusinessContent(businessId, businessContentId);
        return ResponseEntity.noContent().build();
    }
}
