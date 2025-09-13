package com.vn.payments.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
}