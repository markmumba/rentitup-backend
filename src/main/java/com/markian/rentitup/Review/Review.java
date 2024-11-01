package com.markian.rentitup.Review;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.mapping.Join;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Review extends BaseEntity {

    private Integer machineRating;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
