package com.jenesano.directory.service;

import com.jenesano.directory.entity.Review;
import com.jenesano.directory.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return null;
    }

    public Optional<Review> getReviewById(Long id) {
        return Optional.empty();
    }

    public Review createReview(Review review) {
        return null;
    }

    public Review updateReview(Long id, Review updatedReview) {
        return null;
    }

    public void deleteReview(Long id) {

    }
}
