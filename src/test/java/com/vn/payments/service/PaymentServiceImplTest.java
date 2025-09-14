package com.vn.payments.service;

import com.vn.payments.exception.PaymentException;
import com.vn.payments.model.Invoice;
import com.vn.payments.model.Payment;
import com.vn.payments.repository.InvoiceRepository;
import com.vn.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    //@InjectMocks
    private UUID invoiceId;
    private Invoice invoice;
    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceId = UUID.randomUUID();
        invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setAmount(BigDecimal.valueOf(1000));
        invoice.setPaidAmount(BigDecimal.valueOf(200));
        invoice.setPayments(new ArrayList<>());

        payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(300));
    }

    @Test
    void testCreatePayment_Success() throws PaymentException {
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Payment result = paymentService.createPayment(invoiceId, payment);

        assertNotNull(result);
        assertEquals(payment, result);
        assertEquals(BigDecimal.valueOf(500), invoice.getPaidAmount());
        assertEquals(Invoice.Status.PENDING, invoice.getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void testCreatePayment_InvoiceNotFound() {
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            paymentService.createPayment(invoiceId, payment);
        });

        assertEquals("Invoice not found with ID: " + invoiceId, exception.getMessage());
        verify(paymentRepository, never()).save(any());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void testCreatePayment_InvoiceAlreadyPaid() {
        invoice.setPaidAmount(BigDecimal.valueOf(1000));
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            paymentService.createPayment(invoiceId, payment);
        });

        assertEquals("Invoice already paid, Invoice Id:" + invoiceId, exception.getMessage());
        verify(paymentRepository, never()).save(any());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void testCreatePayment_Overpayment() {
        payment.setAmount(BigDecimal.valueOf(900));
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            paymentService.createPayment(invoiceId, payment);
        });

        assertEquals("Incorrect amount paid, remaining:800, Invoice Id:" + invoiceId, exception.getMessage());
        verify(paymentRepository, never()).save(any());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void testGetPayment_Success() throws PaymentException {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        invoice.setPayments(payments);
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        List<Payment> result = paymentService.getPayment(invoiceId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
        verify(invoiceRepository, times(1)).findById(invoiceId);
    }

    @Test
    void testGetPayment_InvoiceNotFound() {
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            paymentService.getPayment(invoiceId);
        });

        assertEquals("Invoice not found with ID: " + invoiceId, exception.getMessage());
        verify(invoiceRepository, times(1)).findById(invoiceId);
    }
}