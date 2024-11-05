package com.markian.rentitup.Review;

import com.markian.rentitup.Exceptions.BookingException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Exceptions.ReviewException;
import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.Review.ReviewDto.ReviewRequestDto;
import com.markian.rentitup.Review.ReviewDto.ReviewResponseDto;
import com.markian.rentitup.Review.ReviewDto.UpdateDto;

import java.util.List;

public interface ReviewService {

    ReviewResponseDto createReview(Long bookingId, ReviewRequestDto reviewRequestDto) throws BookingException, ReviewException, UserException;

    List<ReviewResponseDto> getReviewsForMachine(Long machineId) throws MachineException;

    ReviewResponseDto getReviewById(Long id) throws ReviewException;

    String updateReview(Long id, UpdateDto updateDto) throws ReviewException;

    String deleteReview(Long id) throws ReviewException;
}
