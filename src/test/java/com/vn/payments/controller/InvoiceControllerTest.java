package com.vn.payments.controller;

import com.vn.payments.exception.PaymentException;
import com.vn.payments.model.Invoice;
import com.vn.payments.model.Overdue;
import com.vn.payments.model.Payment;
import com.vn.payments.service.InvoiceService;
import com.vn.payments.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private InvoiceController invoiceController;

    private UUID invoiceId;
    private Invoice invoice;
    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceId = UUID.randomUUID();
        invoice = new Invoice();
        payment = new Payment();
    }

    @Test
    void testPostInvoice() {
        when(invoiceService.createInvoice(invoice)).thenReturn(invoice);

        Invoice result = invoiceController.postInvoice(invoice);

        assertNotNull(result);
        assertEquals(invoice, result);
        verify(invoiceService, times(1)).createInvoice(invoice);
    }

    @Test
    void testGetInvoices() {
        List<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);

        List<Invoice> result = invoiceController.getInvoices();

        assertEquals(1, result.size());
        assertEquals(invoice, result.get(0));
        verify(invoiceService, times(1)).getAllInvoices();
    }

    @Test
    void testGetInvoice() {
        when(invoiceService.getInvoice(invoiceId)).thenReturn(Optional.of(invoice));

        Invoice result = invoiceController.getInvoice(invoiceId);

        assertEquals(invoice, result);
        verify(invoiceService, times(1)).getInvoice(invoiceId);
    }

    @Test
    void testDeleteInvoice() {
        doNothing().when(invoiceService).deleteInvoice(invoiceId);

        invoiceController.deleteInvoice(invoiceId);

        verify(invoiceService, times(1)).deleteInvoice(invoiceId);
    }

    @Test
    void testPostPayment() throws PaymentException{
        when(paymentService.createPayment(invoiceId, payment)).thenReturn(payment);

        Payment result = invoiceController.postPayment(payment, invoiceId);

        assertEquals(payment, result);
        verify(paymentService, times(1)).createPayment(invoiceId, payment);
    }

    @Test
    void testGetAllPayments() throws PaymentException {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.getPayment(invoiceId)).thenReturn(payments);

        List<Payment> result = invoiceController.getAllPayments(invoiceId);

        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
        verify(paymentService, times(1)).getPayment(invoiceId);
    }

    @Test
    void testProcessOverdue() {
        Overdue overdue = new Overdue();
        List<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.processOverdue(overdue)).thenReturn(invoices);

        List<Invoice> result = invoiceController.processOverdue(overdue);

        assertEquals(1, result.size());
        assertEquals(invoice, result.get(0));
        verify(invoiceService, times(1)).processOverdue(overdue);
    }
}
