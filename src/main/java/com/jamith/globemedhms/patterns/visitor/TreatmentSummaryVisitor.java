package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Patient;

public class TreatmentSummaryVisitor implements ReportVisitor {
    @Override
    public String visit(Patient patient) {
        return "Treatment Summary for " + patient.getName() + ":\n" +
               "Medical History: " + (patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "N/A") + "\n" +
               "Treatment Plan: " + (patient.getTreatmentPlan() != null ? patient.getTreatmentPlan() : "N/A");
    }

    @Override
    public String visit(Appointment appointment) {
        return "Treatment Summary for Appointment ID " + appointment.getId() + ":\n" +
               "Treatment Details: " + (appointment.getTreatmentDetails() != null ? appointment.getTreatmentDetails() : "N/A") + "\n" +
               "Prescription: " + (appointment.getPrescription() != null ? appointment.getPrescription() : "N/A");
    }

    @Override
    public String visit(Billing billing) {
        return "Treatment Summary not applicable for Billing ID " + billing.getId();
    }
}