package com.markian.rentitup.Review.ReviewDto;

import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Review.Review;
import com.markian.rentitup.User.User;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {

    public Review toEntity(ReviewRequestDto dto, Booking booking, User reviewer) {
        Review review = new Review();
        review.setMachineRating(dto.getMachineRating());
        review.setComment(dto.getComment());
        review.setBooking(booking);
        review.setReviewer(reviewer);
        return review;
    }

    public ReviewResponseDto toDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setMachineRating(review.getMachineRating());
        dto.setComment(review.getComment());
        dto.setBookingId(review.getBooking().getId());
        dto.setBookingReference(review.getBooking().getBookingCode()); // Assuming `reference` field in `Booking`
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewerName(review.getReviewer().getFullName()); // Assuming `fullName` in `User`
        return dto;
    }
}
