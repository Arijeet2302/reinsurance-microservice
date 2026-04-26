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
 * DTO for creating/updating an Insurance Policy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequest {

    @NotBlank(message = "Policy number is required")
    @Size(min = 3, max = 50, message = "Policy number must be between 3 and 50 characters")
    @JsonProperty("policy_number")
    private String policyNumber;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    @JsonProperty("customer_name")
    private String customerName;

    @Email(message = "Customer email should be valid")
    @JsonProperty("customer_email")
    private String customerEmail;

    @NotBlank(message = "Policy type is required")
    @JsonProperty("policy_type")
    private String policyType;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than 0")
    @JsonProperty("premium_amount")
    private BigDecimal premiumAmount;

    @NotNull(message = "Reinsurance percentage is required")
    @Min(value = 0, message = "Reinsurance percentage must be >= 0")
    @Max(value = 100, message = "Reinsurance percentage must be <= 100")
    @JsonProperty("reinsurance_percentage")
    private BigDecimal reinsurancePercentage;

    @NotNull(message = "Policy start date is required")
    @JsonProperty("policy_start_date")
    private LocalDateTime policyStartDate;

    @NotNull(message = "Policy end date is required")
    @JsonProperty("policy_end_date")
    private LocalDateTime policyEndDate;

    @NotBlank(message = "Provider name is required")
    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("external_policy_id")
    private String externalPolicyId;
}
