package com.markian.rentitup.Review.ReviewDto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponseDto {

    private Long id;
    private Integer machineRating;
    private String comment;
    private Long bookingId;
    private String bookingReference; // Optional: e.g., booking code or number
    private Long reviewerId;
    private String reviewerName; // Optional: reviewer’s full name or username
}
