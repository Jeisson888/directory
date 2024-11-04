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
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getWebsiteReviews() {
        return reviewRepository.findByTypeReview(TypeReview.WEBSITE);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Reseña", reviewId));
    }

    public Review createReview(ReviewDTO reviewDTO) {
        //validar
        Review review = new Review(
                reviewDTO.getReview(),
                reviewDTO.getDescription(),
                LocalDate.now(),
                TypeReview.WEBSITE,
                userService.getUserById(reviewDTO.getUserId())
        );

        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = getReviewById(reviewId);

        //validar
        review.setReview(reviewDTO.getReview());
        review.setDescription(reviewDTO.getDescription());

        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Reseña", reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }
}
