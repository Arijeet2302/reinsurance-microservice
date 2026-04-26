package com.insurance.reinsurance.service;

import com.insurance.reinsurance.dto.ReinsuranceTransactionRequest;
import com.insurance.reinsurance.dto.ReinsuranceTransactionResponse;
import com.insurance.reinsurance.entity.ReinsuranceTransaction;
import com.insurance.reinsurance.exception.ResourceNotFoundException;
import com.insurance.reinsurance.repository.InsurancePolicyRepository;
import com.insurance.reinsurance.repository.ReinsuranceTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing Reinsurance Transactions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final ReinsuranceTransactionRepository transactionRepository;
    private final InsurancePolicyRepository policyRepository;

    /**
     * Create a new reinsurance transaction
     */
    @Transactional
    public ReinsuranceTransactionResponse createTransaction(ReinsuranceTransactionRequest request) {
        log.info("Creating transaction for policy ID: {}", request.getPolicyId());

        // Validate policy exists
        policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + request.getPolicyId()));

        String transactionReference = generateTransactionReference();

        ReinsuranceTransaction transaction = ReinsuranceTransaction.builder()
                .transactionReference(transactionReference)
                .policyId(request.getPolicyId())
                .transactionType(ReinsuranceTransaction.TransactionType.valueOf(request.getTransactionType().toUpperCase()))
                .transactionAmount(request.getTransactionAmount())
                .transactionDate(request.getTransactionDate())
                .description(request.getDescription())
                .status(ReinsuranceTransaction.TransactionStatus.PENDING)
                .approvalStatus(ReinsuranceTransaction.ApprovalStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .referenceDocumentId(request.getReferenceDocumentId())
                .build();

        ReinsuranceTransaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully: {} (ID: {})", transactionReference, savedTransaction.getId());

        return ReinsuranceTransactionResponse.fromEntity(savedTransaction);
    }

    /**
     * Get all transactions
     */
    public List<ReinsuranceTransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(ReinsuranceTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get transaction by ID
     */
    public ReinsuranceTransactionResponse getTransactionById(Long id) {
        ReinsuranceTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
        return ReinsuranceTransactionResponse.fromEntity(transaction);
    }

    /**
     * Get transactions by policy ID
     */
    public List<ReinsuranceTransactionResponse> getTransactionsByPolicyId(Long policyId) {
        // Validate policy exists
        policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + policyId));

        return transactionRepository.findByPolicyId(policyId).stream()
                .map(ReinsuranceTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get transactions by status
     */
    public List<ReinsuranceTransactionResponse> getTransactionsByStatus(String status) {
        ReinsuranceTransaction.TransactionStatus transactionStatus =
                ReinsuranceTransaction.TransactionStatus.valueOf(status.toUpperCase());
        return transactionRepository.findByStatus(transactionStatus).stream()
                .map(ReinsuranceTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get transactions by type
     */
    public List<ReinsuranceTransactionResponse> getTransactionsByType(String type) {
        ReinsuranceTransaction.TransactionType transactionType =
                ReinsuranceTransaction.TransactionType.valueOf(type.toUpperCase());
        return transactionRepository.findByTransactionType(transactionType).stream()
                .map(ReinsuranceTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get transactions by date range
     */
    public List<ReinsuranceTransactionResponse> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsByDateRange(startDate, endDate).stream()
                .map(ReinsuranceTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Approve a transaction
     */
    @Transactional
    public ReinsuranceTransactionResponse approveTransaction(Long id, String approvedBy) {
        ReinsuranceTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        transaction.setApprovalStatus(ReinsuranceTransaction.ApprovalStatus.APPROVED);
        transaction.setApprovedBy(approvedBy);
        transaction.setApprovalDate(LocalDateTime.now());
        transaction.setStatus(ReinsuranceTransaction.TransactionStatus.COMPLETED);

        ReinsuranceTransaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction approved: {} by {}", transaction.getTransactionReference(), approvedBy);

        return ReinsuranceTransactionResponse.fromEntity(updatedTransaction);
    }

    /**
     * Reject a transaction
     */
    @Transactional
    public ReinsuranceTransactionResponse rejectTransaction(Long id, String approvedBy) {
        ReinsuranceTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        transaction.setApprovalStatus(ReinsuranceTransaction.ApprovalStatus.REJECTED);
        transaction.setApprovedBy(approvedBy);
        transaction.setApprovalDate(LocalDateTime.now());
        transaction.setStatus(ReinsuranceTransaction.TransactionStatus.REJECTED);

        ReinsuranceTransaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction rejected: {} by {}", transaction.getTransactionReference(), approvedBy);

        return ReinsuranceTransactionResponse.fromEntity(updatedTransaction);
    }

    /**
     * Get pending approvals
     */
    public List<ReinsuranceTransactionResponse> getPendingApprovals() {
        return transactionRepository.findPendingApprovals().stream()
                .map(ReinsuranceTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get transaction analytics
     */
    public TransactionAnalytics getTransactionAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        List<ReinsuranceTransaction> transactions = transactionRepository.findTransactionsByDateRange(startDate, endDate);

        BigDecimal totalAmount = transactions.stream()
                .filter(t -> t.getStatus() == ReinsuranceTransaction.TransactionStatus.COMPLETED)
                .map(ReinsuranceTransaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalTransactions = transactions.size();
        long completedTransactions = transactions.stream()
                .filter(t -> t.getStatus() == ReinsuranceTransaction.TransactionStatus.COMPLETED)
                .count();

        return TransactionAnalytics.builder()
                .totalTransactions(totalTransactions)
                .completedTransactions(completedTransactions)
                .pendingTransactions(transactionRepository.countByStatus(ReinsuranceTransaction.TransactionStatus.PENDING))
                .totalAmount(totalAmount)
                .build();
    }

    /**
     * Generate unique transaction reference
     */
    private String generateTransactionReference() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * DTO for transaction analytics
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class TransactionAnalytics {
        private Long totalTransactions;
        private Long completedTransactions;
        private Long pendingTransactions;
        private BigDecimal totalAmount;
    }
}
