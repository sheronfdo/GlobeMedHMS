package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.*;
import com.jamith.globemedhms.patterns.composite.ReportComposite;
import com.jamith.globemedhms.patterns.composite.ReportSection;

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
        ReportComposite report = new ReportComposite("Financial Report for Billing ID " + billing.getId());

        ReportSection amountSection = new ReportSection("Amount");
        amountSection.setContent("$" + billing.getAmount());

        ReportSection statusSection = new ReportSection("Status");
        statusSection.setContent(billing.getStatus());

        ReportSection typeSection = new ReportSection("Billing Type");
        typeSection.setContent(billing.getBillingType());

        report.addSection(amountSection);
        report.addSection(statusSection);
        report.addSection(typeSection);

        return report.generate();
    }

    @Override
    public String visit(InsuranceClaim claim) {
        ReportComposite report = new ReportComposite("Financial Report for Insurance Claim ID " + claim.getId());

        ReportSection providerSection = new ReportSection("Insurance Provider");
        providerSection.setContent(claim.getInsuranceProvider());

        ReportSection statusSection = new ReportSection("Claim Status");
        statusSection.setContent(claim.getStatus());

        ReportSection amountSection = new ReportSection("Amount Covered");
        amountSection.setContent(claim.getBilling().getAmount() != 0 ? "LKR " + claim.getBilling().getAmount() : "N/A");

        report.addSection(providerSection);
        report.addSection(statusSection);
        report.addSection(amountSection);

        return report.generate();
    }

    @Override
    public String visit(Staff staff) {
        return "Financial Report not applicable for Staff: " + staff.getName();
    }
}