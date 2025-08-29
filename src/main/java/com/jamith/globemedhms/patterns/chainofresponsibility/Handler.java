package com.jamith.globemedhms.patterns.chainofresponsibility;

import com.jamith.globemedhms.core.entities.InsuranceClaim;

public abstract class Handler {
    protected Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handle(InsuranceClaim claim);
}