package com.insurance.reinsurance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insurance.reinsurance.entity.ReinsuranceTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning Reinsurance Transaction data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReinsuranceTransactionResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("transaction_reference")
    private String transactionReference;

    @JsonProperty("policy_id")
    private Long policyId;

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("transaction_amount")
    private BigDecimal transactionAmount;

    @JsonProperty("transaction_date")
    private LocalDateTime transactionDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("approval_status")
    private String approvalStatus;

    @JsonProperty("approved_by")
    private String approvedBy;

    @JsonProperty("approval_date")
    private LocalDateTime approvalDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("reference_document_id")
    private String referenceDocumentId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static ReinsuranceTransactionResponse fromEntity(ReinsuranceTransaction transaction) {
        return ReinsuranceTransactionResponse.builder()
                .id(transaction.getId())
                .transactionReference(transaction.getTransactionReference())
                .policyId(transaction.getPolicyId())
                .transactionType(transaction.getTransactionType().toString())
                .transactionAmount(transaction.getTransactionAmount())
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .status(transaction.getStatus().toString())
                .approvalStatus(transaction.getApprovalStatus() != null ? transaction.getApprovalStatus().toString() : null)
                .approvedBy(transaction.getApprovedBy())
                .approvalDate(transaction.getApprovalDate())
                .paymentMethod(transaction.getPaymentMethod())
                .referenceDocumentId(transaction.getReferenceDocumentId())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
