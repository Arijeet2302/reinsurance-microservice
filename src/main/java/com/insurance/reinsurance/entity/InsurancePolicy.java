package com.insurance.reinsurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing an Insurance Policy synced from the Policy Admin System
 */
@Entity
@Table(name = "insurance_policies", indexes = {
    @Index(name = "idx_policy_number", columnList = "policy_number"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_sync_source", columnList = "sync_source")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true, length = 50)
    private String policyNumber;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Column(name = "policy_type", nullable = false, length = 50)
    private String policyType; // e.g., HEALTH, AUTO, PROPERTY

    @Column(name = "premium_amount", nullable = false)
    private BigDecimal premiumAmount;

    @Column(name = "reinsurance_percentage", nullable = false)
    private BigDecimal reinsurancePercentage; // e.g., 25.00

    @Column(name = "reinsurance_premium_amount", nullable = false)
    private BigDecimal reinsurancePremiumAmount; // Calculated: premiumAmount * reinsurancePercentage / 100

    @Column(name = "policy_start_date", nullable = false)
    private LocalDateTime policyStartDate;

    @Column(name = "policy_end_date", nullable = false)
    private LocalDateTime policyEndDate;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PolicyStatus status; // ACTIVE, INACTIVE, EXPIRED, PENDING

    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName; // Insurance provider name

    @Column(name = "sync_source", nullable = false, length = 50)
    private String syncSource; // e.g., POLICY_ADMIN_SYSTEM

    @Column(name = "external_policy_id", length = 100)
    private String externalPolicyId; // ID from source system

    @Column(name = "sync_timestamp", nullable = false)
    private LocalDateTime syncTimestamp;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version; // For optimistic locking

    public enum PolicyStatus {
        ACTIVE, INACTIVE, EXPIRED, PENDING
    }
}