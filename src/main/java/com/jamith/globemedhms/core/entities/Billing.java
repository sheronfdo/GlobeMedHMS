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

    @Column(name = "billing_type", nullable = false)
    private String billingType;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "payment_method")
    private String paymentMethod;


    public Billing() {}

//    public Billing(Appointment appointment, double amount, String status) {
//        this.appointment = appointment;
//        this.amount = amount;
//        this.status = status;
//    }

    public Billing(Appointment appointment, double amount, String status, String billingType) {
        this.appointment = appointment;
        this.amount = amount;
        this.status = status;
        this.billingType = billingType;
        this.paymentDate = null;
        this.paymentMethod = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBillingType() { return billingType; }
    public void setBillingType(String billingType) { this.billingType = billingType; }
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("Bill #%d - %s - $%.2f - %s",
                id,
                appointment.getPatient().getName(),
                amount,
                appointment.getDate());
    }
}