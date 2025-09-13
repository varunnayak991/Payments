package com.vn.payments.serivce;

import com.vn.payments.entity.Invoice;
import com.vn.payments.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements  InvoiceService{

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public Optional<Invoice> getInvoice(java.util.UUID id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(UUID id) {
        invoiceRepository.deleteById(id);
    }
}
