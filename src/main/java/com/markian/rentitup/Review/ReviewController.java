package com.markian.rentitup.Review;


import com.markian.rentitup.Review.ReviewDto.ReviewRequestDto;
import com.markian.rentitup.Review.ReviewDto.ReviewResponseDto;
import com.markian.rentitup.Review.ReviewDto.UpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews/machine/{machineId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByMachine(
            @PathVariable Long machineId
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsForMachine(machineId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/bookings/{bookingId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long bookingId,
            @RequestBody ReviewRequestDto reviewRequestDto
    ) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(bookingId, reviewRequestDto);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(
            @PathVariable Long id
    ) {
        ReviewResponseDto reviewResponseDto = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/reviews/{id}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long id,
            @RequestBody UpdateDto updateDto
    ) {

        String response = reviewService.updateReview(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long id
    ) {

        String response = reviewService.deleteReview(id);
        return ResponseEntity.ok(response);
    }
}


