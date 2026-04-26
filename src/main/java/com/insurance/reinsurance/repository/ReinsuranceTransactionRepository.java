package com.insurance.reinsurance.repository;

import com.insurance.reinsurance.entity.ReinsuranceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ReinsuranceTransaction entity
 */
@Repository
public interface ReinsuranceTransactionRepository extends JpaRepository<ReinsuranceTransaction, Long> {

    /**
     * Find a transaction by reference
     */
    Optional<ReinsuranceTransaction> findByTransactionReference(String transactionReference);

    /**
     * Find all transactions for a policy
     */
    List<ReinsuranceTransaction> findByPolicyId(Long policyId);

    /**
     * Find all transactions by status
     */
    List<ReinsuranceTransaction> findByStatus(ReinsuranceTransaction.TransactionStatus status);

    /**
     * Find all transactions by type
     */
    List<ReinsuranceTransaction> findByTransactionType(ReinsuranceTransaction.TransactionType transactionType);

    /**
     * Find transactions by date range
     */
    @Query("SELECT t FROM ReinsuranceTransaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<ReinsuranceTransaction> findTransactionsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions for a policy by date range
     */
    @Query("SELECT t FROM ReinsuranceTransaction t WHERE t.policyId = :policyId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<ReinsuranceTransaction> findTransactionsByPolicyAndDateRange(@Param("policyId") Long policyId,
                                                                      @Param("startDate") LocalDateTime startDate,
                                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total transaction amount by type
     */
    @Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM ReinsuranceTransaction t WHERE t.transactionType = :type AND t.status = 'COMPLETED'")
    BigDecimal getTotalAmountByType(@Param("type") ReinsuranceTransaction.TransactionType type);

    /**
     * Calculate total transaction amount for a policy
     */
    @Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM ReinsuranceTransaction t WHERE t.policyId = :policyId AND t.status = 'COMPLETED'")
    BigDecimal getTotalAmountByPolicy(@Param("policyId") Long policyId);

    /**
     * Count pending transactions
     */
    long countByStatus(ReinsuranceTransaction.TransactionStatus status);

    /**
     * Find pending approvals
     */
    @Query("SELECT t FROM ReinsuranceTransaction t WHERE t.approvalStatus = 'PENDING' ORDER BY t.createdAt ASC")
    List<ReinsuranceTransaction> findPendingApprovals();
}
