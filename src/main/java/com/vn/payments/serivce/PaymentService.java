package com.vn.payments.serivce;

import com.vn.payments.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    public Payment createPayment(UUID invoiceId, Payment payment);

    public Payment getPayment(UUID invoiceId);

}
