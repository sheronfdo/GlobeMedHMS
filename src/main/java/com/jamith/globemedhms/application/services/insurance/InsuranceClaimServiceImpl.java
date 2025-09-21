package com.jamith.globemedhms.application.services.insurance;

import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.InsuranceClaim;
import com.jamith.globemedhms.infrastructure.repository.InsuranceClaimRepository;
import com.jamith.globemedhms.patterns.chainofresponsibility.Handler;
import com.jamith.globemedhms.patterns.chainofresponsibility.ApproveClaimHandler;
import com.jamith.globemedhms.patterns.chainofresponsibility.CheckEligibilityHandler;
import com.jamith.globemedhms.patterns.chainofresponsibility.ProcessPaymentHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class InsuranceClaimServiceImpl implements InsuranceClaimService {
    private static final Logger logger = LogManager.getLogger(InsuranceClaimServiceImpl.class);
    private InsuranceClaimRepository insuranceClaimRepository = new InsuranceClaimRepository();

    @Override
    public List<InsuranceClaim> getAllClaims() {
        return insuranceClaimRepository.getAllClaims();
    }

    @Override
    public void saveOrUpdateClaim(InsuranceClaim claim) {
        insuranceClaimRepository.saveOrUpdate(claim);
    }

    @Override
    public InsuranceClaim getClaimById(int id) {
        return insuranceClaimRepository.findById(id);
    }

    @Override
    public InsuranceClaim getClaimByBillingId(int billingId) {
        return insuranceClaimRepository.findByBillingId(billingId);
    }

    @Override
    public InsuranceClaim createClaim(Billing billing, String insuranceProvider, String policyNumber) {
        // Check if billing is eligible for insurance claim
        if (!"INSURANCE".equals(billing.getBillingType())) {
            throw new IllegalArgumentException("Billing is not for insurance");
        }

        // Check if claim already exists
        InsuranceClaim existingClaim = getClaimByBillingId(billing.getId());
        if (existingClaim != null) {
            throw new IllegalStateException("Claim already exists for this billing");
        }

        InsuranceClaim claim = new InsuranceClaim(billing, "PENDING", insuranceProvider, policyNumber);
        saveOrUpdateClaim(claim);

        logger.info("Created insurance claim for billing ID: {}", billing.getId());
        return claim;
    }

    @Override
    public InsuranceClaim processClaim(InsuranceClaim claim) {
        // Chain of Responsibility for claim processing
        Handler eligibilityHandler = new CheckEligibilityHandler();
        Handler approvalHandler = new ApproveClaimHandler();
        Handler paymentHandler = new ProcessPaymentHandler();

        eligibilityHandler.setNextHandler(approvalHandler);
        approvalHandler.setNextHandler(paymentHandler);

        eligibilityHandler.handle(claim);

        saveOrUpdateClaim(claim);
        logger.info("Processed claim for billing ID: {}", claim.getBilling().getId());
        return claim;
    }
}