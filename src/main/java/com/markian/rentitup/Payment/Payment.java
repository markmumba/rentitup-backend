package com.markian.rentitup.Payment;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Booking.Booking;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "payment")
public class Payment extends BaseEntity {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public enum PaymentType {
        DEPOSIT,
        FULL_PAYMENT,
        SECURITY_DEPOSIT
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        REFUNDED,
        FAILED
    }

}
