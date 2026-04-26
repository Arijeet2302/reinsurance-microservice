package com.insurance.reinsurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a Reinsurance Transaction
 */
@Entity
@Table(name = "reinsurance_transactions", indexes = {
    @Index(name = "idx_transaction_ref", columnList = "transaction_reference"),
    @Index(name = "idx_policy_id", columnList = "policy_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_transaction_date", columnList = "transaction_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReinsuranceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_reference", nullable = false, unique = true, length = 50)
    private String transactionReference;

    @Column(name = "policy_id", nullable = false)
    private Long policyId;

    @Column(name = "transaction_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // PREMIUM_PAYMENT, CLAIM_SETTLEMENT, ADJUSTMENT, REFUND

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, COMPLETED, FAILED, REJECTED

    @Column(name = "approval_status", length = 20)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus; // PENDING, APPROVED, REJECTED

    @Column(name = "approved_by", length = 100)
    private String approvedBy; // User who approved the transaction

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // e.g., BANK_TRANSFER, CHEQUE, NEFT

    @Column(name = "reference_document_id", length = 100)
    private String referenceDocumentId; // Invoice, claim ID, etc.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    public enum TransactionType {
        PREMIUM_PAYMENT, CLAIM_SETTLEMENT, ADJUSTMENT, REFUND
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, REJECTED
    }

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
}