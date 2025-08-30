package com.jamith.globemedhms.patterns.builder;

import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.patterns.decorator.SanitizationDecorator;

public class PatientBuilder {
    private String name;
    private String dateOfBirth;
    private String address;
    private String medicalHistory;
    private String treatmentPlan;
    private String history;

    public PatientBuilder setName(String name) {
        this.name = SanitizationDecorator.sanitize(name);
        return this;
    }

    public PatientBuilder setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = SanitizationDecorator.sanitize(dateOfBirth);
        return this;
    }

    public PatientBuilder setAddress(String address) {
        this.address = SanitizationDecorator.sanitize(address);
        return this;
    }

    public PatientBuilder setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory != null ? EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(medicalHistory)) : null;
        return this;
    }

    public PatientBuilder setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan != null ? EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(treatmentPlan)) : null;
        return this;
    }

    public PatientBuilder setHistory(String history) {
        this.history = history != null ? EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(history)) : "";
        return this;
    }

    public Patient build() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            throw new IllegalArgumentException("Date of Birth is required");
        }
        Patient patient = new Patient();
        patient.setName(name);
        patient.setDateOfBirth(dateOfBirth);
        patient.setAddress(address);
        patient.setMedicalHistory(medicalHistory);
        patient.setTreatmentPlan(treatmentPlan);
        patient.setHistory(history != null ? history : "");
        return patient;
    }
}