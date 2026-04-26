package com.insurance.reinsurance.controller;

import com.insurance.reinsurance.dto.ApiResponse;
import com.insurance.reinsurance.dto.ReinsuranceTransactionRequest;
import com.insurance.reinsurance.dto.ReinsuranceTransactionResponse;
import com.insurance.reinsurance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for Reinsurance Transaction Management
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Create a new reinsurance transaction
     * POST /api/v1/transactions
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReinsuranceTransactionResponse>> createTransaction(
            @Valid @RequestBody ReinsuranceTransactionRequest request) {
        log.info("Transaction creation request received for policy ID: {}", request.getPolicyId());
        ReinsuranceTransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Transaction created successfully"));
    }

    /**
     * Get all transactions
     * GET /api/v1/transactions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReinsuranceTransactionResponse>>> getAllTransactions() {
        log.info("Fetching all transactions");
        List<ReinsuranceTransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved successfully"));
    }

    /**
     * Get transaction by ID
     * GET /api/v1/transactions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReinsuranceTransactionResponse>> getTransactionById(@PathVariable Long id) {
        log.info("Fetching transaction with ID: {}", id);
        ReinsuranceTransactionResponse transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Transaction retrieved successfully"));
    }

    /**
     * Get transactions by policy ID
     * GET /api/v1/transactions/policy/{policyId}
     */
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<ReinsuranceTransactionResponse>>> getTransactionsByPolicyId(
            @PathVariable Long policyId) {
        log.info("Fetching transactions for policy ID: {}", policyId);
        List<ReinsuranceTransactionResponse> transactions = transactionService.getTransactionsByPolicyId(policyId);
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved by policy"));
    }

    /**
     * Get transactions by status
     * GET /api/v1/transactions/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ReinsuranceTransactionResponse>>> getTransactionsByStatus(
            @PathVariable String status) {
        log.info("Fetching transactions with status: {}", status);
        List<ReinsuranceTransactionResponse> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved by status"));
    }

    /**
     * Get transactions by type
     * GET /api/v1/transactions/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ReinsuranceTransactionResponse>>> getTransactionsByType(
            @PathVariable String type) {
        log.info("Fetching transactions with type: {}", type);
        List<ReinsuranceTransactionResponse> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved by type"));
    }

    /**
     * Get transactions by date range
     * GET /api/v1/transactions/date-range?startDate={startDate}&endDate={endDate}
     */
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<ReinsuranceTransactionResponse>>> getTransactionsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        log.info("Fetching transactions between {} and {}", startDate, endDate);
        List<ReinsuranceTransactionResponse> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved by date range"));
    }

    /**
     * Approve a transaction
     * PATCH /api/v1/transactions/{id}/approve
     */
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ReinsuranceTransactionResponse>> approveTransaction(
            @PathVariable Long id,
            @RequestParam String approvedBy) {
        log.info("Approving transaction with ID: {} by: {}", id, approvedBy);
        ReinsuranceTransactionResponse transaction = transactionService.approveTransaction(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Transaction approved successfully"));
    }

    /**
     * Reject a transaction
     * PATCH /api/v1/transactions/{id}/reject
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ReinsuranceTransactionResponse>> rejectTransaction(
            @PathVariable Long id,
            @RequestParam String approvedBy) {
        log.info("Rejecting transaction with ID: {} by: {}", id, approvedBy);
        ReinsuranceTransactionResponse transaction = transactionService.rejectTransaction(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Transaction rejected successfully"));
    }

    /**
     * Get pending approvals
     * GET /api/v1/transactions/approvals/pending
     */
    @GetMapping("/approvals/pending")
    public ResponseEntity<ApiResponse<List<ReinsuranceTransactionResponse>>> getPendingApprovals() {
        log.info("Fetching pending approvals");
        List<ReinsuranceTransactionResponse> transactions = transactionService.getPendingApprovals();
        return ResponseEntity.ok(ApiResponse.success(transactions, "Pending approvals retrieved successfully"));
    }

    /**
     * Get transaction analytics
     * GET /api/v1/transactions/analytics?startDate={startDate}&endDate={endDate}
     */
    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<TransactionAnalyticsResponse>> getTransactionAnalytics(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        log.info("Fetching transaction analytics between {} and {}", startDate, endDate);
        TransactionService.TransactionAnalytics analytics = transactionService.getTransactionAnalytics(startDate, endDate);
        
        TransactionAnalyticsResponse response = TransactionAnalyticsResponse.builder()
                .totalTransactions(analytics.getTotalTransactions())
                .completedTransactions(analytics.getCompletedTransactions())
                .pendingTransactions(analytics.getPendingTransactions())
                .totalAmount(analytics.getTotalAmount())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response, "Transaction analytics retrieved successfully"));
    }

    /**
     * Health check endpoint
     * GET /api/v1/transactions/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("OK", "Transaction Service is healthy"));
    }

    /**
     * DTO for transaction analytics response
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class TransactionAnalyticsResponse {
        private Long totalTransactions;
        private Long completedTransactions;
        private Long pendingTransactions;
        private java.math.BigDecimal totalAmount;
    }
}
