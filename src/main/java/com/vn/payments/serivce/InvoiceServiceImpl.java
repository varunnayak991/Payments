package com.vn.payments.serivce;

import com.vn.payments.entity.Invoice;
import com.vn.payments.entity.Overdue;
import com.vn.payments.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Override
    @Transactional
    public List<Invoice> processOverdue(Overdue overdue) {

        List<Invoice> overdueInvoices = new ArrayList<>();
        List<Invoice> invoices = invoiceRepository.findAll();

        for(Invoice invoice : invoices)
        {
            if(invoice.getStatus().equals(Invoice.Status.PENDING) && invoice.getDueDate().isBefore(LocalDate.now()))
            {
                BigDecimal pendingAmount = invoice.getAmount().subtract(invoice.getPaidAmount());
                invoice.setStatus(Invoice.Status.VOID);
                invoiceRepository.save(invoice);

                Invoice newInvoice = new Invoice();
                newInvoice.setAmount(pendingAmount.add(overdue.getLateFee()));
                newInvoice.setDueDate(invoice.getDueDate().plusDays(overdue.getOverdueDays()));
                newInvoice.setStatus(Invoice.Status.PENDING);
                invoiceRepository.save(newInvoice);
                overdueInvoices.add(newInvoice);
            }
        }
        return overdueInvoices;
    }
}
