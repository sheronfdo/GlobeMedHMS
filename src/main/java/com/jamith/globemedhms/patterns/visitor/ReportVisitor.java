package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Patient;

public interface ReportVisitor {
    String visit(Patient patient);
    String visit(Appointment appointment);
    String visit(Billing billing);
}