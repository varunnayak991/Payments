package com.vn.payments.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Represents an Invoice entity. This class is mapped to the "invoices" table in the database.
 */
@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Invoice {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private BigDecimal amount;

    private BigDecimal paidAmount = BigDecimal.ZERO;

    private LocalDate dueDate;

    private Status status = Status.PENDING;

    @OneToOne
    private Payment payment;

    public enum Status{
        PENDING("pending"),
        PAID("paid"),
        VOID("void");

        private final String stringValue;

        Status(String stringValue) {
            this.stringValue = stringValue;
        }
        public String getStringValue() {
            return stringValue;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}