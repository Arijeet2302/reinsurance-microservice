package com.insurance.reinsurance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for batch syncing multiple policies
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchPolicySyncRequest {

    @NotEmpty(message = "Policies list cannot be empty")
    @JsonProperty("policies")
    private List<@Valid InsurancePolicyRequest> policies;
}
