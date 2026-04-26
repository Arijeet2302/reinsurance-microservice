package com.insurance.reinsurance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insurance.reinsurance.entity.InsurancePolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning Insurance Policy data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("policy_number")
    private String policyNumber;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("policy_type")
    private String policyType;

    @JsonProperty("premium_amount")
    private BigDecimal premiumAmount;

    @JsonProperty("reinsurance_percentage")
    private BigDecimal reinsurancePercentage;

    @JsonProperty("reinsurance_premium_amount")
    private BigDecimal reinsurancePremiumAmount;

    @JsonProperty("policy_start_date")
    private LocalDateTime policyStartDate;

    @JsonProperty("policy_end_date")
    private LocalDateTime policyEndDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("sync_source")
    private String syncSource;

    @JsonProperty("sync_timestamp")
    private LocalDateTime syncTimestamp;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static InsurancePolicyResponse fromEntity(InsurancePolicy policy) {
        return InsurancePolicyResponse.builder()
                .id(policy.getId())
                .policyNumber(policy.getPolicyNumber())
                .customerName(policy.getCustomerName())
                .customerEmail(policy.getCustomerEmail())
                .policyType(policy.getPolicyType())
                .premiumAmount(policy.getPremiumAmount())
                .reinsurancePercentage(policy.getReinsurancePercentage())
                .reinsurancePremiumAmount(policy.getReinsurancePremiumAmount())
                .policyStartDate(policy.getPolicyStartDate())
                .policyEndDate(policy.getPolicyEndDate())
                .status(policy.getStatus().toString())
                .providerName(policy.getProviderName())
                .syncSource(policy.getSyncSource())
                .syncTimestamp(policy.getSyncTimestamp())
                .createdAt(policy.getCreatedAt())
                .updatedAt(policy.getUpdatedAt())
                .build();
    }
}
