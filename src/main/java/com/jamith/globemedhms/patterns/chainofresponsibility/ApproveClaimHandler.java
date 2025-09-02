package com.jamith.globemedhms.patterns.chainofresponsibility;

import com.jamith.globemedhms.core.entities.InsuranceClaim;

public class ApproveClaimHandler extends Handler {
    @Override
    public void handle(InsuranceClaim claim) {
        if (claim.getStatus().equals("ELIGIBLE")) {
            claim.setStatus("APPROVED");
        } else {
            claim.setStatus("REJECTED");
            return;
        }
        if (nextHandler != null) {
            nextHandler.handle(claim);
        }
    }
}