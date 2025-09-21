package com.jamith.globemedhms.patterns.chainofresponsibility;

import com.jamith.globemedhms.core.entities.InsuranceClaim;

public class CheckEligibilityHandler extends Handler {
    @Override
    public void handle(InsuranceClaim claim) {
        if (claim.getInsuranceProvider() != null && !claim.getInsuranceProvider().isEmpty() &&
                claim.getPolicyNumber() != null && !claim.getPolicyNumber().isEmpty()) {
            claim.setStatus("ELIGIBLE");
        } else {
            claim.setStatus("INELIGIBLE");
            return;
        }
        if (nextHandler != null) {
            nextHandler.handle(claim);
        }
    }
}