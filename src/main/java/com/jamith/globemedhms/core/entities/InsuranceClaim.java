package com.jamith.globemedhms.core.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "billing_id", nullable = false)
    private Billing billing;

    @Column(nullable = false)
    private String status; // e.g., "PENDING", "APPROVED", "REJECTED"

    public InsuranceClaim() {}

    public InsuranceClaim(Billing billing, String status) {
        this.billing = billing;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}