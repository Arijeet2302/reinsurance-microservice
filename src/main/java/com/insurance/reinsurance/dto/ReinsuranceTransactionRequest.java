package com.insurance.reinsurance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for creating/updating a Reinsurance Transaction
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReinsuranceTransactionRequest {

    @NotNull(message = "Policy ID is required")
    @JsonProperty("policy_id")
    private Long policyId;

    @NotBlank(message = "Transaction type is required")
    @JsonProperty("transaction_type")
    private String transactionType; // PREMIUM_PAYMENT, CLAIM_SETTLEMENT, ADJUSTMENT, REFUND

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @JsonProperty("transaction_amount")
    private BigDecimal transactionAmount;

    @NotNull(message = "Transaction date is required")
    @JsonProperty("transaction_date")
    private LocalDateTime transactionDate;

    @JsonProperty("description")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("reference_document_id")
    private String referenceDocumentId;
}
