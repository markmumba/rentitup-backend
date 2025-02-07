package com.markian.rentitup.Booking;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Payment.Payment;
import com.markian.rentitup.Review.Review;
import com.markian.rentitup.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "booking")
public class Booking extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String bookingCode;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String pickUpLocation;

    @Enumerated(EnumType.STRING)
    private BookingStatus status ;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

}
