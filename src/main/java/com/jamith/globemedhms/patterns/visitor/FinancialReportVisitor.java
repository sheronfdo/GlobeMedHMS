package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Patient;

public class FinancialReportVisitor implements ReportVisitor {
    @Override
    public String visit(Patient patient) {
        return "Financial Report not applicable for Patient: " + patient.getName();
    }

    @Override
    public String visit(Appointment appointment) {
        return "Financial Report not applicable for Appointment ID: " + appointment.getId();
    }

    @Override
    public String visit(Billing billing) {
        return "Financial Report for Billing ID " + billing.getId() + ":\n" +
               "Amount: $" + billing.getAmount() + "\n" +
               "Status: " + billing.getStatus();
    }
}