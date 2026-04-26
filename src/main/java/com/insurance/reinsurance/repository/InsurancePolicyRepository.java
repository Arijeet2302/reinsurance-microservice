package com.insurance.reinsurance.repository;

import com.insurance.reinsurance.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for InsurancePolicy entity
 */
@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

    /**
     * Find a policy by policy number
     */
    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);

    /**
     * Find all policies by status
     */
    List<InsurancePolicy> findByStatus(InsurancePolicy.PolicyStatus status);

    /**
     * Find all policies by provider name
     */
    List<InsurancePolicy> findByProviderName(String providerName);

    /**
     * Find all policies by sync source
     */
    List<InsurancePolicy> findBySyncSource(String syncSource);

    /**
     * Find policies synced after a specific timestamp
     */
    @Query("SELECT p FROM InsurancePolicy p WHERE p.syncTimestamp >= :syncTime ORDER BY p.syncTimestamp DESC")
    List<InsurancePolicy> findPoliciesSyncedAfter(@Param("syncTime") LocalDateTime syncTime);

    /**
     * Find policies by date range
     */
    @Query("SELECT p FROM InsurancePolicy p WHERE p.policyStartDate BETWEEN :startDate AND :endDate")
    List<InsurancePolicy> findPoliciesByDateRange(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total reinsurance premium amount for a provider
     */
    @Query("SELECT COALESCE(SUM(p.reinsurancePremiumAmount), 0) FROM InsurancePolicy p WHERE p.providerName = :providerName AND p.status = 'ACTIVE'")
    BigDecimal getTotalReinsurancePremiumByProvider(@Param("providerName") String providerName);

    /**
     * Count active policies
     */
    long countByStatus(InsurancePolicy.PolicyStatus status);

    /**
     * Check if a policy exists by external ID
     */
    boolean existsByExternalPolicyId(String externalPolicyId);

    /**
     * Find policy by external ID
     */
    Optional<InsurancePolicy> findByExternalPolicyId(String externalPolicyId);
}
