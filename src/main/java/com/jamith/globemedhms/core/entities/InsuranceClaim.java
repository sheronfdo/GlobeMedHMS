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
    private String status;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "policy_number")
    private String policyNumber;

    public InsuranceClaim() {}

//    public InsuranceClaim(Billing billing, String status) {
//        this.billing = billing;
//        this.status = status;
//    }

    public InsuranceClaim(Billing billing, String status, String insuranceProvider, String policyNumber) {
        this.billing = billing;
        this.status = status;
        this.insuranceProvider = insuranceProvider;
        this.policyNumber = policyNumber;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
}