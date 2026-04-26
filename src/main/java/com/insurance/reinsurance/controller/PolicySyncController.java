package com.insurance.reinsurance.controller;

import com.insurance.reinsurance.dto.ApiResponse;
import com.insurance.reinsurance.dto.BatchPolicySyncRequest;
import com.insurance.reinsurance.dto.InsurancePolicyRequest;
import com.insurance.reinsurance.dto.InsurancePolicyResponse;
import com.insurance.reinsurance.service.PolicySyncService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for Policy Synchronization from Policy Admin System
 */
@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
@Slf4j
public class PolicySyncController {

    private final PolicySyncService policySyncService;

    /**
     * Sync a single policy from Policy Admin System
     * POST /api/v1/policies/sync
     */
    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<InsurancePolicyResponse>> syncPolicy(
            @Valid @RequestBody InsurancePolicyRequest request) {
        log.info("Policy sync request received for: {}", request.getPolicyNumber());
        InsurancePolicyResponse response = policySyncService.syncPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Policy synced successfully"));
    }

    /**
     * Sync multiple policies in batch
     * POST /api/v1/policies/sync/batch
     */
    @PostMapping("/sync/batch")
    public ResponseEntity<ApiResponse<List<InsurancePolicyResponse>>> syncPoliciesBatch(
            @Valid @RequestBody BatchPolicySyncRequest request) {
        log.info("Batch policy sync request received for {} policies", request.getPolicies().size());
        List<InsurancePolicyResponse> responses = policySyncService.syncPoliciesBatch(request.getPolicies());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responses, "Batch policies synced successfully"));
    }

    /**
     * Get all policies
     * GET /api/v1/policies
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InsurancePolicyResponse>>> getAllPolicies() {
        log.info("Fetching all policies");
        List<InsurancePolicyResponse> policies = policySyncService.getAllPolicies();
        return ResponseEntity.ok(ApiResponse.success(policies, "Policies retrieved successfully"));
    }

    /**
     * Get policy by ID
     * GET /api/v1/policies/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsurancePolicyResponse>> getPolicyById(@PathVariable Long id) {
        log.info("Fetching policy with ID: {}", id);
        InsurancePolicyResponse policy = policySyncService.getPolicyById(id);
        return ResponseEntity.ok(ApiResponse.success(policy, "Policy retrieved successfully"));
    }

    /**
     * Get policies by status
     * GET /api/v1/policies/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<InsurancePolicyResponse>>> getPoliciesByStatus(
            @PathVariable String status) {
        log.info("Fetching policies with status: {}", status);
        List<InsurancePolicyResponse> policies = policySyncService.getPoliciesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(policies, "Policies retrieved by status"));
    }

    /**
     * Get policies by provider
     * GET /api/v1/policies/provider/{providerName}
     */
    @GetMapping("/provider/{providerName}")
    public ResponseEntity<ApiResponse<List<InsurancePolicyResponse>>> getPoliciesByProvider(
            @PathVariable String providerName) {
        log.info("Fetching policies from provider: {}", providerName);
        List<InsurancePolicyResponse> policies = policySyncService.getPoliciesByProvider(providerName);
        return ResponseEntity.ok(ApiResponse.success(policies, "Policies retrieved by provider"));
    }

    /**
     * Get policies synced after a specific time
     * GET /api/v1/policies/synced-after?timestamp={timestamp}
     */
    @GetMapping("/synced-after")
    public ResponseEntity<ApiResponse<List<InsurancePolicyResponse>>> getPoliciesSyncedAfter(
            @RequestParam LocalDateTime timestamp) {
        log.info("Fetching policies synced after: {}", timestamp);
        List<InsurancePolicyResponse> policies = policySyncService.getPoliciesSyncedAfter(timestamp);
        return ResponseEntity.ok(ApiResponse.success(policies, "Policies retrieved by sync time"));
    }

    /**
     * Update policy status
     * PATCH /api/v1/policies/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InsurancePolicyResponse>> updatePolicyStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("Updating policy status for ID: {} to: {}", id, status);
        InsurancePolicyResponse policy = policySyncService.updatePolicyStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(policy, "Policy status updated successfully"));
    }

    /**
     * Health check endpoint
     * GET /api/v1/policies/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("OK", "Policy Sync Service is healthy"));
    }
}
