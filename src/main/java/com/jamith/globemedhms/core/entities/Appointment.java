package com.jamith.globemedhms.core.entities;

import com.jamith.globemedhms.patterns.visitor.ReportVisitor;
import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String type;

    @Column
    private String status;

    @Column(columnDefinition = "TEXT")
    private String prescription;

    @Column(columnDefinition = "TEXT")
    private String treatmentDetails;

    public Appointment() {}

    public Appointment(Staff staff, Patient patient, String date, String time, String type, String status) {
        this.staff = staff;
        this.patient = patient;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public String getTreatmentDetails() { return treatmentDetails; }
    public void setTreatmentDetails(String treatmentDetails) { this.treatmentDetails = treatmentDetails; }

    @Override
    public String toString() {
        return "Appointment with " + staff.getName() + " on " + date + " at " + time;
    }

    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}