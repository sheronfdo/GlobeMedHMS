package com.jamith.globemedhms.patterns.chainofresponsibility;

import com.jamith.globemedhms.core.entities.InsuranceClaim;

public class ProcessPaymentHandler extends Handler {
    @Override
    public void handle(InsuranceClaim claim) {
        if (claim.getStatus().equals("APPROVED")) {
            claim.setStatus("PAID");
        }
        if (nextHandler != null) {
            nextHandler.handle(claim);
        }
    }
}