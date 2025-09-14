package com.vn.payments.service;

import com.vn.payments.exception.PaymentException;
import com.vn.payments.model.Invoice;
import com.vn.payments.model.Payment;
import com.vn.payments.repository.InvoiceRepository;
import com.vn.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public Payment createPayment(UUID invoiceId, Payment payment) throws PaymentException {

        Optional<Invoice> optInvoice = invoiceRepository.findById(invoiceId);

        if(! optInvoice.isPresent())
        {
            throw new PaymentException("Invoice not found with ID: " + invoiceId);
        }

        Invoice invoice = optInvoice.get();

        BigDecimal amount = invoice.getAmount();
        BigDecimal paid = invoice.getPaidAmount();
        BigDecimal remaining = amount.subtract(paid);

        if(remaining.floatValue() <= 0 ) throw new PaymentException("Invoice already paid, Invoice Id:"+ invoiceId);

        BigDecimal updatedPaid = paid.add(payment.getAmount());
        BigDecimal updatedRemaining = remaining.subtract(payment.getAmount());

        if(updatedRemaining.floatValue() < 0 ) throw new PaymentException("Incorrect amount paid, remaining:"+remaining+", Invoice Id:"+ invoiceId);

        if(updatedRemaining.floatValue() > 0 ) {

            invoice.setPaidAmount(updatedPaid);
            invoice.setStatus(Invoice.Status.PENDING);

            payment.setInvoice(invoice);
            paymentRepository.save(payment);

            List<Payment> payments = invoice.getPayments();
                    payments.add(payment);
            invoice.setPayments(payments);
            invoiceRepository.save(invoice);
        }
        else if(updatedRemaining.floatValue() == 0 ) {
            payment.setInvoice(invoice);
            paymentRepository.save(payment);

            List<Payment> payments = invoice.getPayments();
            payments.add(payment);
            invoice.setPaidAmount(updatedPaid);
            invoice.setStatus(Invoice.Status.PAID);
            invoiceRepository.save(invoice);
        }
        return payment;
    }


    public List<Payment> getPayment(UUID invoiceId) throws PaymentException {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new PaymentException("Invoice not found with ID: " + invoiceId));

        return invoice.getPayments();

    }
}
