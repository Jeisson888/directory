package com.jenesano.directory.service;

import com.jenesano.directory.dto.ReviewDTO;
import com.jenesano.directory.entity.Review;
import com.jenesano.directory.entity.TypeReview;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private ReviewDTO reviewDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("Test User");
        review = new Review(5, "Buen sitio web", LocalDate.now(), TypeReview.WEBSITE, user);
        review.setId(1L);

        reviewDTO = new ReviewDTO();
        reviewDTO.setReview(4);
        reviewDTO.setDescription("Good experience");
        reviewDTO.setUserId(1L);
    }

    @Test
    public void testGetWebsiteReviews() {
        when(reviewRepository.findByTypeReview(TypeReview.WEBSITE)).thenReturn(List.of(review));

        List<Review> websiteReviews = reviewService.getWebsiteReviews();

        assertNotNull(websiteReviews);
        assertEquals(1, websiteReviews.size());
        assertEquals("Buen sitio web", websiteReviews.getFirst().getDescription());
    }

    @Test
    public void testCreateReview() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(reviewRepository.save(ArgumentMatchers.any(Review.class))).thenReturn(review);

        Review createdReview = reviewService.createReview(reviewDTO);

        assertNotNull(createdReview);
        assertEquals(5, createdReview.getReview());
        assertEquals("Buen sitio web", createdReview.getDescription());
        assertEquals(TypeReview.WEBSITE, createdReview.getTypeReview());
        assertEquals("Test User", createdReview.getUser().getUsername());
    }
}
