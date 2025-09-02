package com.jamith.globemedhms.core.entities;

import com.jamith.globemedhms.patterns.visitor.ReportVisitor;
import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column
    private String address;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(columnDefinition = "TEXT")
    private String history;

    public Patient() {}

    public Patient(String name, String dateOfBirth, String address, String medicalHistory, String treatmentPlan) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.medicalHistory = medicalHistory;
        this.treatmentPlan = treatmentPlan;
        this.history = "";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getTreatmentPlan() { return treatmentPlan; }
    public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }
    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }

    public void appendHistory(String entry) {
        this.history += "\n" + entry;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }

    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}