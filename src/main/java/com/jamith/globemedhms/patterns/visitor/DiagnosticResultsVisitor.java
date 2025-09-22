package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.*;
import com.jamith.globemedhms.patterns.composite.ReportComposite;
import com.jamith.globemedhms.patterns.composite.ReportSection;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;

public class DiagnosticResultsVisitor implements ReportVisitor {
    @Override
    public String visit(Patient patient) {
        ReportComposite report = new ReportComposite("Diagnostic Results for " + patient.getName());

        ReportSection historySection = new ReportSection("Medical History");
        historySection.setContent(patient.getMedicalHistory() != null ?
                EncryptionDecorator.decrypt(patient.getMedicalHistory()) : "N/A");

        report.addSection(historySection);
        return report.generate();
    }

    @Override
    public String visit(Appointment appointment) {
        ReportComposite report = new ReportComposite("Diagnostic Results for Appointment ID " + appointment.getId());

        ReportSection typeSection = new ReportSection("Appointment Type");
        typeSection.setContent(appointment.getType());

        ReportSection detailsSection = new ReportSection("Treatment Details");
        detailsSection.setContent(appointment.getTreatmentDetails() != null ?
                EncryptionDecorator.decrypt(appointment.getTreatmentDetails()) : "N/A");

        report.addSection(typeSection);
        report.addSection(detailsSection);

        return report.generate();
    }

    @Override
    public String visit(Billing billing) {
        return "Diagnostic Results not applicable for Billing ID " + billing.getId();
    }

    @Override
    public String visit(InsuranceClaim claim) {
        return "Diagnostic Results not applicable for Insurance Claim ID " + claim.getId();
    }

    @Override
    public String visit(Staff staff) {
        return "Diagnostic Results not applicable for Staff: " + staff.getName();
    }
}