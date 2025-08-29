package com.jamith.globemedhms.patterns.chainofresponsibility;

import com.jamith.globemedhms.core.entities.InsuranceClaim;

public class CheckEligibilityHandler extends Handler {
    @Override
    public void handle(InsuranceClaim claim) {
        // Simulate eligibility check
        if (true) { // Replace with real logic, e.g., check insurance status
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