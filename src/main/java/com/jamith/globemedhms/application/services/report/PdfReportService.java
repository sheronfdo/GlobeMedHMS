package com.jamith.globemedhms.application.services.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jamith.globemedhms.core.entities.*;
import com.jamith.globemedhms.patterns.builder.PdfReportBuilder;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReportService {
    private static final Logger logger = LogManager.getLogger(PdfReportService.class);
    
    public static String generatePatientReport(Patient patient, String reportType) {
        return new PdfReportBuilder()
            .setTitle("Patient " + reportType + " Report")
            .setEntityType("Patient")
            .setEntity(patient)
            .addField("Patient ID", String.valueOf(patient.getId()))
            .addField("Name", patient.getName())
            .addField("Date of Birth", patient.getDateOfBirth())
            .addField("Address", patient.getAddress())
            .addField("Medical History", 
                     patient.getMedicalHistory() != null ? 
                     EncryptionDecorator.decrypt(patient.getMedicalHistory()) : "N/A")
            .addField("Treatment Plan", 
                     patient.getTreatmentPlan() != null ? 
                     EncryptionDecorator.decrypt(patient.getTreatmentPlan()) : "N/A")
            .build();
    }
    
    public static String generateAppointmentReport(Appointment appointment, String reportType) {
        PdfReportBuilder builder = new PdfReportBuilder()
            .setTitle("Appointment " + reportType + " Report")
            .setEntityType("Appointment")
            .setEntity(appointment)
            .addField("Appointment ID", String.valueOf(appointment.getId()))
            .addField("Patient", appointment.getPatient().getName())
            .addField("Staff", appointment.getStaff().getName())
            .addField("Date", appointment.getDate())
            .addField("Time", appointment.getTime())
            .addField("Type", appointment.getType())
            .addField("Status", appointment.getStatus());

        if ("Treatment Summary".equals(reportType) || "Diagnostic Results".equals(reportType)) {
            builder.addField("Treatment Details", 
                appointment.getTreatmentDetails() != null ? 
                EncryptionDecorator.decrypt(appointment.getTreatmentDetails()) : "N/A");
            
            if ("Treatment Summary".equals(reportType)) {
                builder.addField("Prescription", 
                    appointment.getPrescription() != null ? 
                    EncryptionDecorator.decrypt(appointment.getPrescription()) : "N/A");
            }
        }
        
        return builder.build();
    }
    
    public static String generateBillingReport(Billing billing, String reportType) {
        return new PdfReportBuilder()
            .setTitle("Billing " + reportType + " Report")
            .setEntityType("Billing")
            .setEntity(billing)
            .addField("Billing ID", String.valueOf(billing.getId()))
            .addField("Appointment ID", String.valueOf(billing.getAppointment().getId()))
            .addField("Patient", billing.getAppointment().getPatient().getName())
            .addField("Amount", "$" + billing.getAmount())
            .addField("Status", billing.getStatus())
            .addField("Billing Type", billing.getBillingType())
            .build();
    }
    
    public static String generateInsuranceClaimReport(InsuranceClaim claim, String reportType) {
        return new PdfReportBuilder()
            .setTitle("Insurance Claim " + reportType + " Report")
            .setEntityType("Insurance Claim")
            .setEntity(claim)
            .addField("Claim ID", String.valueOf(claim.getId()))
            .addField("Billing ID", String.valueOf(claim.getBilling().getId()))
            .addField("Insurance Provider", claim.getInsuranceProvider())
            .addField("Policy Number", claim.getPolicyNumber())
            .addField("Status", claim.getStatus())
            .addField("Amount Covered", 
                     claim.getBilling().getAmount() != 0 ? "$" + claim.getBilling().getAmount() : "N/A")
//            .addField("Patient Responsibility",
//                     claim.getPatientResponsibility() != null ? "$" + claim.getPatientResponsibility() : "N/A")
            .build();
    }
}