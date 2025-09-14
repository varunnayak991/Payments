package com.vn.payments.service;

import com.vn.payments.model.Invoice;
import com.vn.payments.model.Overdue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface InvoiceService {
    public Optional<Invoice> getInvoice(UUID id);
    public List<Invoice> getAllInvoices();

    public Invoice createInvoice(Invoice invoice);

    public void deleteInvoice(UUID id);

    public List<Invoice> processOverdue(Overdue overdue);
}
