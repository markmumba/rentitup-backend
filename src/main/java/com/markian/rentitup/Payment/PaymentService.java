package com.markian.rentitup.Payment;

import java.util.List;

public interface PaymentService {

    Payment makePayment (Payment payment);
    List<Payment>  getAllPayments(Long bookingId);


}
