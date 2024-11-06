package com.markian.rentitup.Review.ReviewDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ReviewRequestDto {

    @NotNull(message = "Machine rating is required")
    @Min(value = 1, message = "Rating should be at least 1")
    @Max(value = 5, message = "Rating should be no more than 5")
    private Integer machineRating;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;
}