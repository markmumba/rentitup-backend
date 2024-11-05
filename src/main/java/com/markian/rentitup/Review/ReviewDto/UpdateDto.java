package com.markian.rentitup.Review.ReviewDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateDto {
    private Integer machineRating;
    private String comment;
}
