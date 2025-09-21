package com.jamith.globemedhms.application.services.insurance;

import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.InsuranceClaim;

import java.util.List;

public interface InsuranceClaimService {
    List<InsuranceClaim> getAllClaims();
    void saveOrUpdateClaim(InsuranceClaim claim);
    InsuranceClaim getClaimById(int id);
    InsuranceClaim getClaimByBillingId(int billingId);
    InsuranceClaim createClaim(Billing billing, String insuranceProvider, String policyNumber);
    InsuranceClaim processClaim(InsuranceClaim claim);
}