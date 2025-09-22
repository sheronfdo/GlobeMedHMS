package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.*;
import com.jamith.globemedhms.patterns.composite.ReportComposite;
import com.jamith.globemedhms.patterns.composite.ReportSection;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;

public class TreatmentSummaryVisitor implements ReportVisitor {
    @Override
    public String visit(Patient patient) {
        ReportComposite report = new ReportComposite("Treatment Summary for " + patient.getName());

        ReportSection historySection = new ReportSection("Medical History");
        historySection.setContent(patient.getMedicalHistory() != null ?
                EncryptionDecorator.decrypt(patient.getMedicalHistory()) : "N/A");

        ReportSection planSection = new ReportSection("Treatment Plan");
        planSection.setContent(patient.getTreatmentPlan() != null ?
                EncryptionDecorator.decrypt(patient.getTreatmentPlan()) : "N/A");

        report.addSection(historySection);
        report.addSection(planSection);

        return report.generate();
    }

    @Override
    public String visit(Appointment appointment) {
        ReportComposite report = new ReportComposite("Treatment Summary for Appointment ID " + appointment.getId());

        ReportSection detailsSection = new ReportSection("Treatment Details");
        detailsSection.setContent(appointment.getTreatmentDetails() != null ?
                EncryptionDecorator.decrypt(appointment.getTreatmentDetails()) : "N/A");

        ReportSection prescriptionSection = new ReportSection("Prescription");
        prescriptionSection.setContent(appointment.getPrescription() != null ?
                EncryptionDecorator.decrypt(appointment.getPrescription()) : "N/A");

        report.addSection(detailsSection);
        report.addSection(prescriptionSection);

        return report.generate();
    }

    @Override
    public String visit(Billing billing) {
        return "Treatment Summary not applicable for Billing ID " + billing.getId();
    }

    @Override
    public String visit(InsuranceClaim claim) {
        return "Treatment Summary not applicable for Insurance Claim ID " + claim.getId();
    }

    @Override
    public String visit(Staff staff) {
        return "Treatment Summary not applicable for Staff: " + staff.getName();
    }
}