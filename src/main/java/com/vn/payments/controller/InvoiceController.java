package com.vn.payments.controller;

import com.vn.payments.entity.Invoice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Invoices and Payments API", description = "Endpoints for payments and invoices")
public class PaymentsController {

    @GetMapping("/invoices")
    @Operation(summary = "Returns all payment", description = "get all payments in the system.")
    public List<Invoice> getList() {
        return Collections.emptyList();
    }
}