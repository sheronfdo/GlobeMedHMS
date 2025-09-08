package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;

public class DiagnosticResultsVisitor implements ReportVisitor {
    @Override
    public String visit(Patient patient) {
        return "Diagnostic Results for " + patient.getName() + ":\n" +
               "Medical History: " + (patient.getMedicalHistory() != null ? EncryptionDecorator.decrypt(patient.getMedicalHistory()) : "N/A");
    }

    @Override
    public String visit(Appointment appointment) {
        return "Diagnostic Results for Appointment ID " + appointment.getId() + ":\n" +
               "Type: " + appointment.getType() + "\n" +
               "Treatment Details: " + (appointment.getTreatmentDetails() != null ? EncryptionDecorator.decrypt(appointment.getTreatmentDetails()) : "N/A");
    }

    @Override
    public String visit(Billing billing) {
        return "Diagnostic Results not applicable for Billing ID " + billing.getId();
    }
}