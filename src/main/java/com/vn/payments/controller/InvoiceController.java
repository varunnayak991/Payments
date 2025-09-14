package com.vn.payments.controller;

import com.vn.payments.entity.Invoice;
import com.vn.payments.entity.Overdue;
import com.vn.payments.entity.Payment;
import com.vn.payments.serivce.InvoiceService;
import com.vn.payments.serivce.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Invoices and Payments API", description = "Endpoints for payments and invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    PaymentService paymentService;

    @PostMapping("/invoice")
    @Operation(summary = "Create an invoice", description = "Create a new invoice.")
    public @ResponseBody Invoice postInvoice(@RequestBody Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }
    @GetMapping("/invoice")
    @Operation(summary = "Returns all invoices", description = "get all invoices in the system.")
    public List<Invoice> getInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Returns invoice by Id", description = "get an invoice by an Id")
    public @ResponseBody Invoice getInvoice(@PathVariable UUID invoiceId) {
        return invoiceService.getInvoice(invoiceId).get();
    }

    @DeleteMapping("/invoice/{invoiceId}")
    @Operation(summary = "Delete invoice", description = "Deletes an invoice by Id.")
    public void deleteInvoice(@PathVariable UUID invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    @PostMapping("/invoice/{invoiceId}/payments")
    @Operation(summary = "Create an Payment", description = "Create a payment against an Invoice.")
    public @ResponseBody Payment postPayment(@RequestBody Payment payment, @PathVariable UUID invoiceId) throws IllegalArgumentException{
       return paymentService.createPayment(invoiceId,payment);
    }

    @GetMapping("/invoice/{invoiceId}/payments")
    @Operation(summary = "Gets Payment for an ivoice", description = "Gets  payment against an Invoice.")
    public @ResponseBody List<Payment> getAllPayments(@PathVariable UUID invoiceId){
        return paymentService.getPayment(invoiceId);
    }

    @GetMapping("/invoice/process-overdue")
    @Operation(summary = "Process overdue invoices", description = "Processes all the overdue invoices, applies fees and pushes days")
    public @ResponseBody List<Invoice> processOverdue(Overdue overdue){
        return invoiceService.processOverdue(overdue);
    }
}