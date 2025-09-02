package com.jamith.globemedhms.core.entities;

import com.jamith.globemedhms.patterns.visitor.ReportVisitor;
import jakarta.persistence.*;

@Entity
@Table(name = "billings")
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String status;

    public Billing() {}

    public Billing(Appointment appointment, double amount, String status) {
        this.appointment = appointment;
        this.amount = amount;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}