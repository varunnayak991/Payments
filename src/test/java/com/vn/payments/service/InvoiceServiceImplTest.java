package com.vn.payments.service;

import com.vn.payments.model.Invoice;
import com.vn.payments.model.Overdue;
import com.vn.payments.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    private UUID invoiceId;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceId = UUID.randomUUID();
        invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setAmount(BigDecimal.valueOf(1000));
        invoice.setPaidAmount(BigDecimal.valueOf(200));
        invoice.setDueDate(LocalDate.now().minusDays(5));
        invoice.setStatus(Invoice.Status.PENDING);
    }

    @Test
    void testGetInvoice_Success() {
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        Optional<Invoice> result = invoiceService.getInvoice(invoiceId);

        assertTrue(result.isPresent());
        assertEquals(invoice, result.get());
        verify(invoiceRepository, times(1)).findById(invoiceId);
    }

    @Test
    void testGetAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice);
        when(invoiceRepository.findAll()).thenReturn(invoices);

        List<Invoice> result = invoiceService.getAllInvoices();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(invoice, result.get(0));
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void testCreateInvoice() {
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice result = invoiceService.createInvoice(invoice);

        assertNotNull(result);
        assertEquals(invoice, result);
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void testDeleteInvoice() {
        doNothing().when(invoiceRepository).deleteById(invoiceId);

        invoiceService.deleteInvoice(invoiceId);

        verify(invoiceRepository, times(1)).deleteById(invoiceId);
    }

    @Test
    void testProcessOverdue() {
        Overdue overdue = new Overdue();
        overdue.setLateFee(BigDecimal.valueOf(50));
        overdue.setOverdueDays(10L);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice);
        when(invoiceRepository.findAll()).thenReturn(invoices);

        List<Invoice> result = invoiceService.processOverdue(overdue);

        assertNotNull(result);
        assertEquals(1, result.size());
        Invoice newInvoice = result.get(0);
        assertEquals(BigDecimal.valueOf(850), newInvoice.getAmount());
        assertEquals(invoice.getDueDate().plusDays(10), newInvoice.getDueDate());
        assertEquals(Invoice.Status.PENDING, newInvoice.getStatus());
        verify(invoiceRepository, times(2)).save(any(Invoice.class));
    }
}