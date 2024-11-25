package com.jenesano.directory.controller;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.Business;
import com.jenesano.directory.entity.TypeBusiness;
import com.jenesano.directory.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Obtener todos los negocios",
            description = "Devuelve una lista con todos los negocios registrados. Requiere rol de ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Business>> getAllBusinesses() {
        List<Business> businesses = businessService.getAllBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocios habilitados",
            description = "Devuelve una lista con todos los negocios habilitados.")
    @GetMapping("/enabled")
    public ResponseEntity<List<Business>> getEnabledBusinesses() {
        List<Business> businesses = businessService.getEnabledBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocios deshabilitados",
            description = "Devuelve una lista con todos los negocios deshabilitados. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/disabled")
    public ResponseEntity<List<Business>> getDisableBusinesses() {
        List<Business> businesses = businessService.getDisabledBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocios no validados",
            description = "Devuelve una lista con todos los negocios que aún no han sido validados. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/non-validated")
    public ResponseEntity<List<Business>> getNonValidatedBusinesses() {
        List<Business> businesses = businessService.getNonValidatedBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocios de un usuario",
            description = "Devuelve una lista con todos los negocios asociados a un usuario específico. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Business>> getAllBusinessesByUser(@PathVariable Long userId) {
        List<Business> businesses = businessService.getAllBusinessesByUser(userId);
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocios por tipo",
            description = "Devuelve una lista con todos los negocios asociados a un tipo de negocio específico.")
    @GetMapping("/type/{typeBusinessId}")
    public ResponseEntity<List<Business>> getAllBusinessesByTypeBusiness(@PathVariable Long typeBusinessId) {
        List<Business> businesses = businessService.getAllBusinessesByTypeBusiness(typeBusinessId);
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocios por reseña",
            description = "Devuelve una lista con todos los negocios que tienen una calificación específica.")
    @GetMapping("/review/{review}")
    public ResponseEntity<List<Business>> getAllBusinessesByReview(@PathVariable int review) {
        List<Business> businesses = businessService.getAllBusinessesByReview(review);
        return ResponseEntity.ok(businesses);
    }

    @Operation(summary = "Obtener negocio por ID",
            description = "Devuelve los detalles de un negocio específico basado en su ID.")
    @GetMapping("/{businessId}")
    public ResponseEntity<Business> getBusinessById(@PathVariable Long businessId) {
        Business business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Obtener promedio de reseñas de un negocio",
            description = "Devuelve el promedio de calificaciones de un negocio específico basado en su ID.")
    @GetMapping("/{businessId}/average-review")
    public ResponseEntity<Double> getAverageReview(@PathVariable Long businessId) {
        double averageReview = businessService.getAverageReview(businessId);
        return ResponseEntity.ok(averageReview);
    }

    @Operation(summary = "Crear un nuevo negocio",
            description = "Registra un nuevo negocio en la base de datos.")
    @PostMapping
    public ResponseEntity<Business> createBusiness(@RequestBody BusinessDTO businessDTO) {
        Business business = businessService.createBusiness(businessDTO);
        return new ResponseEntity<>(business, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un negocio",
            description = "Actualiza los detalles de un negocio existente. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OWNER')")
    @PutMapping("/{businessId}")
    public ResponseEntity<Business> updateBusiness(@PathVariable Long businessId, @RequestBody BusinessDTO businessDTO) {
        Business business = businessService.updateBusiness(businessId, businessDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Eliminar un negocio",
            description = "Elimina un negocio de la base de datos. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long businessId) {
        businessService.deleteBusiness(businessId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validar negocio",
            description = "Marca un negocio como validado. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{businessId}/validate")
    public ResponseEntity<Void> validateBusiness(@PathVariable Long businessId) {
        businessService.validateBusiness(businessId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todos los tipos de negocio",
            description = "Devuelve una lista con todos los tipos de negocio disponibles.")
    @GetMapping("/types")
    public ResponseEntity<List<TypeBusiness>> getAllTypesBusinesses() {
        List<TypeBusiness> typesBusiness = businessService.getAllTypesBusiness();
        return ResponseEntity.ok(typesBusiness);
    }

    @Operation(summary = "Obtener un tipo de negocio por ID",
            description = "Devuelve la información de un tipo de negocio específico. Requiere rol de ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/types/{typeBusinessId}")
    public ResponseEntity<TypeBusiness> getTypeBusinessById(@PathVariable Long typeBusinessId) {
        TypeBusiness typeBusiness = businessService.getTypeBusinessById(typeBusinessId);
        return ResponseEntity.ok(typeBusiness);
    }

    @Operation(summary = "Crear un nuevo tipo de negocio",
            description = "Crea un nuevo tipo de negocio en la base de datos. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/types")
    public ResponseEntity<TypeBusiness> createTypeBusiness(@RequestBody TypeBusinessDTO typeBusinessDTO) {
        TypeBusiness typeBusiness = businessService.createTypeBusiness(typeBusinessDTO);
        return new ResponseEntity<>(typeBusiness, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un tipo de negocio",
            description = "Actualiza un tipo de negocio existente. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/types/{typeBusinessId}")
    public ResponseEntity<TypeBusiness> updateTypeBusiness(@PathVariable Long typeBusinessId, @RequestBody TypeBusinessDTO typeBusinessDTO) {
        TypeBusiness typeBusiness = businessService.updateTypeBusiness(typeBusinessId, typeBusinessDTO);
        return ResponseEntity.ok(typeBusiness);
    }

    @Operation(summary = "Eliminar un tipo de negocio",
            description = "Elimina un tipo de negocio de la base de datos. Requiere rol de ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/types/{typeBusinessId}")
    public ResponseEntity<Void> deleteTypeBusiness(@PathVariable Long typeBusinessId) {
        businessService.deleteTypeBusiness(typeBusinessId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Establecer la ubicación de un negocio",
            description = "Configura la ubicación de un negocio específico. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/location")
    public ResponseEntity<Business> setLocation(@PathVariable Long businessId, @RequestBody LocationDTO locationDTO) {
        Business business = businessService.setLocation(businessId, locationDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Establecer horarios de un negocio",
            description = "Define los horarios de atención de un negocio específico. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/hours")
    public ResponseEntity<Business> setBusinessHours(@PathVariable Long businessId, @RequestBody List<BusinessHourDTO> businessHoursDTO) {
        Business business = businessService.setBusinessHours(businessId, businessHoursDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Agregar imagen a un negocio",
            description = "Asocia una imagen al negocio especificado. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/images")
    public ResponseEntity<Business> addImage(@PathVariable Long businessId, @RequestBody ImageDTO imageDTO) {
        Business business = businessService.addImage(businessId, imageDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Eliminar imagen de un negocio",
            description = "Elimina una imagen específica asociada a un negocio. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @DeleteMapping("/{businessId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(@PathVariable Long businessId, @PathVariable Long imageId) {
        businessService.removeImage(businessId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar contenido a un negocio",
            description = "Asocia contenido adicional al negocio especificado. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/{businessId}/contents")
    public ResponseEntity<Business> addBusinessContent(@PathVariable Long businessId, @RequestBody BusinessContentDTO businessContentDTO) {
        Business business = businessService.addBusinessContent(businessId, businessContentDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Actualizar contenido de un negocio",
            description = "Actualiza el contenido asociado a un negocio específico. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PutMapping("/{businessId}/contents/{businessContentId}")
    public ResponseEntity<Business> updateBusinessContent(@PathVariable Long businessId, @PathVariable Long businessContentId, @RequestBody BusinessContentDTO businessContentDTO) {
        Business business = businessService.updateBusinessContent(businessId, businessContentId, businessContentDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Eliminar contenido de un negocio",
            description = "Elimina el contenido asociado a un negocio específico. Requiere rol de ADMIN o OWNER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @DeleteMapping("/{businessId}/contents/{businessContentId}")
    public ResponseEntity<Void> removeBusinessContent(@PathVariable Long businessId, @PathVariable Long businessContentId) {
        businessService.removeBusinessContent(businessId, businessContentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar reseña a un negocio",
            description = "Añade una nueva reseña a un negocio específico. Requiere rol de ADMIN o TOURIST.")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOURIST')")
    @PostMapping("/{businessId}/reviews")
    public ResponseEntity<Business> addReview(@PathVariable Long businessId, @RequestBody ReviewDTO reviewDTO) {
        Business business = businessService.addReview(businessId, reviewDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Actualizar reseña de un negocio",
            description = "Actualiza una reseña existente de un negocio específico. Requiere rol de ADMIN o TOURIST.")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOURIST')")
    @PutMapping("/{businessId}/reviews/{reviewId}")
    public ResponseEntity<Business> updateReview(@PathVariable Long businessId, @PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        Business business = businessService.updateReview(businessId, reviewId, reviewDTO);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "Eliminar reseña de un negocio",
            description = "Elimina una reseña existente de un negocio específico. Requiere rol de ADMIN o TOURIST.")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOURIST')")
    @DeleteMapping("/{businessId}/reviews/{reviewId}")
    public ResponseEntity<Void> removeReview(@PathVariable Long businessId, @PathVariable Long reviewId) {
        businessService.removeReview(businessId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
