package com.vn.payments.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("status")
    private Status status = Status.PENDING;

    @OneToMany
    private List<Payment> payments;

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

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}