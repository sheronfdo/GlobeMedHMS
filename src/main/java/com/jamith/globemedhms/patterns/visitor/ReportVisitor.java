package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.*;

public interface ReportVisitor {
    String visit(Patient patient);
    String visit(Appointment appointment);
    String visit(Billing billing);
    String visit(InsuranceClaim claim);
    String visit(Staff staff);
}