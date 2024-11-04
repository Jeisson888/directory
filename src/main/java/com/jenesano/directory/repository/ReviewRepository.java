package com.jenesano.directory.repository;

import com.jenesano.directory.entity.Review;
import com.jenesano.directory.entity.TypeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTypeReview(TypeReview typeReview);
}
