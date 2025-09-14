package com.vn.payments.service;

import com.vn.payments.exception.PaymentException;
import com.vn.payments.model.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    public Payment createPayment(UUID invoiceId, Payment payment) throws PaymentException;

    public List<Payment> getPayment(UUID invoiceId) throws PaymentException;

}
