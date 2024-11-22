package com.jenesano.directory.service;

import com.jenesano.directory.dto.ReviewDTO;
import com.jenesano.directory.entity.Review;
import com.jenesano.directory.entity.TypeReview;
import com.jenesano.directory.exception.EntityNotFoundException;
import com.jenesano.directory.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    // Obtiene todas las reseñas existentes.
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Obtiene todas las reseñas de tipo WEBSITE.
    public List<Review> getWebsiteReviews() {
        return reviewRepository.findByTypeReview(TypeReview.WEBSITE);
    }

    // Obtiene una reseña específica por su ID. Si no existe, lanza una excepción.
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Reseña", reviewId));
    }

    // Crea una nueva reseña utilizando los datos proporcionados en el DTO.
    public Review createReview(ReviewDTO reviewDTO) {
        validateReviewDTO(reviewDTO);
        Review review = new Review(
                reviewDTO.getReview(),
                reviewDTO.getDescription(),
                LocalDate.now(),
                TypeReview.WEBSITE,
                userService.getUserById(reviewDTO.getUserId())
        );

        return reviewRepository.save(review);
    }

    // Actualiza una reseña existente con los nuevos datos del DTO.
    public Review updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = getReviewById(reviewId);

        validateReviewDTO(reviewDTO);
        review.setReview(reviewDTO.getReview());
        review.setDescription(reviewDTO.getDescription());

        return reviewRepository.save(review);
    }

    // Valida que la reseña esté en el rango de 0 a 5.
    private void validateReviewDTO(ReviewDTO reviewDTO) {
        if (reviewDTO.getReview() < 0 || reviewDTO.getReview() > 5) {
            throw new IllegalArgumentException("La review debe estar entre 0 y 5.");
        }
    }

    // Elimina una reseña por su ID. Lanza una excepción si no se encuentra.
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Reseña", reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }
}
