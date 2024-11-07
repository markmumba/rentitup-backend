package com.markian.rentitup.Review.Impl;

import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Booking.BookingRepository;
import com.markian.rentitup.Exceptions.BookingException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Exceptions.ReviewException;
import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.Review.Review;
import com.markian.rentitup.Review.ReviewDto.ReviewMapper;
import com.markian.rentitup.Review.ReviewDto.ReviewRequestDto;
import com.markian.rentitup.Review.ReviewDto.ReviewResponseDto;
import com.markian.rentitup.Review.ReviewDto.UpdateDto;
import com.markian.rentitup.Review.ReviewRepository;
import com.markian.rentitup.Review.ReviewService;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;


    public ReviewServiceImpl(BookingRepository bookingRepository, ReviewRepository reviewRepository, UserRepository userRepository, ReviewMapper reviewMapper) {
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }


    @Override
    public ReviewResponseDto createReview(Long bookingId, ReviewRequestDto reviewRequestDto) throws BookingException, ReviewException, UserException {
        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                    () -> new BookingException("Booking with id " + bookingId + "not found ")
            );
            User user = userRepository.findById(reviewRequestDto.getReviewerId()).orElseThrow(
                    () -> new UserException("User of id " + reviewRequestDto.getReviewerId() + "not found")
            );
            Review review = reviewMapper.toEntity(reviewRequestDto, booking, user);

            Review savedReview = reviewRepository.save(review);

            return reviewMapper.toDto(savedReview);

        } catch (BookingException | UserException e) {
            throw e;
        } catch (Exception e) {
            throw new ReviewException("Unable to create review " + e.getMessage(), e);
        }
    }

    @Override
    public List<ReviewResponseDto> getReviewsForMachine(Long machineId) throws MachineException {
        try {

            return reviewRepository.findAllByBooking_MachineId(machineId)
                    .stream()
                    .map(reviewMapper::toDto)
                    .toList();

        } catch (Exception e) {
            throw new ReviewException("Unable to get reviews by machine " + e.getMessage(), e);
        }
    }

    @Override
    public ReviewResponseDto getReviewById(Long id) throws ReviewException {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new ReviewException("Review with id " + id + " not found"));

            return reviewMapper.toDto(review);
        } catch (ReviewException e) {
            throw e;
        } catch (Exception e) {
            throw new ReviewException("Unable to get review by id  " + e.getMessage(), e);
        }

    }

    @Override
    public String updateReview(Long id, UpdateDto updateDto) throws ReviewException {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new ReviewException("Review with id " + id + " not found"));

            review.setComment(updateDto.getComment());
            review.setMachineRating(updateDto.getMachineRating());
            reviewRepository.save(review);
            return "Review updated successfully";

        } catch (ReviewException e) {
            throw e;
        } catch (Exception e) {
            throw new ReviewException("Unable to update review " + e.getMessage(), e);
        }
    }

    @Override
    public String deleteReview(Long id) throws ReviewException {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new ReviewException("Review with id " + id + " not found"));

            reviewRepository.delete(review);
            return "review deleted successfully";

        } catch (ReviewException e) {
            throw e;
        } catch (Exception e) {
            throw new ReviewException("Unable to delete review " + e.getMessage(), e);
        }

    }
}
