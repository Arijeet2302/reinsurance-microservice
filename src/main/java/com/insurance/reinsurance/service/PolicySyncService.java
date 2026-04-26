package com.insurance.reinsurance.service;

import com.insurance.reinsurance.dto.InsurancePolicyRequest;
import com.insurance.reinsurance.dto.InsurancePolicyResponse;
import com.insurance.reinsurance.entity.InsurancePolicy;
import com.insurance.reinsurance.exception.ResourceNotFoundException;
import com.insurance.reinsurance.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for syncing insurance policies from Policy Admin System
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PolicySyncService {

    private final InsurancePolicyRepository policyRepository;
    private static final String SYNC_SOURCE = "POLICY_ADMIN_SYSTEM";

    /**
     * Sync a single policy from the Policy Admin System
     */
    @Transactional
    public InsurancePolicyResponse syncPolicy(InsurancePolicyRequest request) {
        log.info("Syncing policy: {}", request.getPolicyNumber());

        // Check if policy already exists
        if (policyRepository.findByPolicyNumber(request.getPolicyNumber()).isPresent()) {
            log.warn("Policy already exists: {}", request.getPolicyNumber());
            throw new IllegalArgumentException("Policy with number " + request.getPolicyNumber() + " already exists");
        }

        // Calculate reinsurance premium
        BigDecimal reinsurancePremium = calculateReinsurancePremium(
                request.getPremiumAmount(),
                request.getReinsurancePercentage()
        );

        InsurancePolicy policy = InsurancePolicy.builder()
                .policyNumber(request.getPolicyNumber())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .policyType(request.getPolicyType())
                .premiumAmount(request.getPremiumAmount())
                .reinsurancePercentage(request.getReinsurancePercentage())
                .reinsurancePremiumAmount(reinsurancePremium)
                .policyStartDate(request.getPolicyStartDate())
                .policyEndDate(request.getPolicyEndDate())
                .status(InsurancePolicy.PolicyStatus.ACTIVE)
                .providerName(request.getProviderName())
                .syncSource(SYNC_SOURCE)
                .externalPolicyId(request.getExternalPolicyId())
                .syncTimestamp(LocalDateTime.now())
                .build();

        InsurancePolicy savedPolicy = policyRepository.save(policy);
        log.info("Policy synced successfully: {} (ID: {})", savedPolicy.getPolicyNumber(), savedPolicy.getId());

        return InsurancePolicyResponse.fromEntity(savedPolicy);
    }

    /**
     * Sync multiple policies in batch
     */
    @Transactional
    public List<InsurancePolicyResponse> syncPoliciesBatch(List<InsurancePolicyRequest> requests) {
        log.info("Starting batch sync of {} policies", requests.size());

        List<InsurancePolicy> policies = requests.stream()
                .map(request -> {
                    // Skip duplicates
                    if (policyRepository.findByPolicyNumber(request.getPolicyNumber()).isPresent()) {
                        log.warn("Skipping duplicate policy: {}", request.getPolicyNumber());
                        return null;
                    }

                    BigDecimal reinsurancePremium = calculateReinsurancePremium(
                            request.getPremiumAmount(),
                            request.getReinsurancePercentage()
                    );

                    return InsurancePolicy.builder()
                            .policyNumber(request.getPolicyNumber())
                            .customerName(request.getCustomerName())
                            .customerEmail(request.getCustomerEmail())
                            .policyType(request.getPolicyType())
                            .premiumAmount(request.getPremiumAmount())
                            .reinsurancePercentage(request.getReinsurancePercentage())
                            .reinsurancePremiumAmount(reinsurancePremium)
                            .policyStartDate(request.getPolicyStartDate())
                            .policyEndDate(request.getPolicyEndDate())
                            .status(InsurancePolicy.PolicyStatus.ACTIVE)
                            .providerName(request.getProviderName())
                            .syncSource(SYNC_SOURCE)
                            .externalPolicyId(request.getExternalPolicyId())
                            .syncTimestamp(LocalDateTime.now())
                            .build();
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        List<InsurancePolicy> savedPolicies = policyRepository.saveAll(policies);
        log.info("Batch sync completed. {} policies saved", savedPolicies.size());

        return savedPolicies.stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all policies
     */
    public List<InsurancePolicyResponse> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get policy by ID
     */
    public InsurancePolicyResponse getPolicyById(Long id) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + id));
        return InsurancePolicyResponse.fromEntity(policy);
    }

    /**
     * Get policies by status
     */
    public List<InsurancePolicyResponse> getPoliciesByStatus(String status) {
        InsurancePolicy.PolicyStatus policyStatus = InsurancePolicy.PolicyStatus.valueOf(status.toUpperCase());
        return policyRepository.findByStatus(policyStatus).stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get policies by provider
     */
    public List<InsurancePolicyResponse> getPoliciesByProvider(String providerName) {
        return policyRepository.findByProviderName(providerName).stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get policies synced after a specific time
     */
    public List<InsurancePolicyResponse> getPoliciesSyncedAfter(LocalDateTime syncTime) {
        return policyRepository.findPoliciesSyncedAfter(syncTime).stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update policy status
     */
    @Transactional
    public InsurancePolicyResponse updatePolicyStatus(Long id, String status) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + id));

        policy.setStatus(InsurancePolicy.PolicyStatus.valueOf(status.toUpperCase()));
        InsurancePolicy updatedPolicy = policyRepository.save(policy);
        log.info("Policy status updated to {} for ID: {}", status, id);

        return InsurancePolicyResponse.fromEntity(updatedPolicy);
    }

    /**
     * Calculate reinsurance premium amount
     */
    private BigDecimal calculateReinsurancePremium(BigDecimal premiumAmount, BigDecimal reinsurancePercentage) {
        return premiumAmount.multiply(reinsurancePercentage).divide(BigDecimal.valueOf(100));
    }
}
