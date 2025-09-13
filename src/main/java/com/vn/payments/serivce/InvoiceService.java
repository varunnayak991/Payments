package com.vn.payments.serivce;

import com.vn.payments.entity.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface InvoiceService {
    public Optional<Invoice> getInvoice(UUID id);
    public List<Invoice> getAllInvoices();

    public Invoice createInvoice(Invoice invoice);

    public void deleteInvoice(UUID id);
}
