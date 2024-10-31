package com.jenesano.directory.repository;

import com.jenesano.directory.entity.Business;
import com.jenesano.directory.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByValidatedAndStatus(boolean validated, Status status);

    List<Business> findByValidated(boolean validated);

    List<Business> findByValidatedAndStatusAndTypeBusinessId(boolean validated, Status status, Long typeBusinessId);

    List<Business> findByValidatedAndStatusAndReviewsReview(boolean validated, Status status, int review);
}
