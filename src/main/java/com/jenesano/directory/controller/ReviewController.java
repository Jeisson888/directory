package com.jenesano.directory.controller;

import com.jenesano.directory.dto.ReviewDTO;
import com.jenesano.directory.entity.Review;
import com.jenesano.directory.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Obtener todas las reseñas",
            description = "Obtiene todas las reseñas almacenadas en el sistema. Solo accesible para ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Obtener reseñas del sitio web",
            description = "Obtiene las reseñas relacionadas con el sitio web. Requiere rol de ADMIN o MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/website")
    public ResponseEntity<List<Review>> getWebsiteReviews() {
        List<Review> reviews = reviewService.getWebsiteReviews();
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Obtener una reseña por ID",
            description = "Obtiene una reseña específica mediante su ID. Requiere rol de ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Crear una nueva reseña",
            description = "Permite a un usuario crear una reseña. Requiere rol de ADMIN o TOURIST.")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOURIST')")
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una reseña",
            description = "Actualiza los detalles de una reseña existente. Solo accesible para ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        Review review = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Eliminar una reseña",
            description = "Elimina una reseña específica mediante su ID. Requiere rol de ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
