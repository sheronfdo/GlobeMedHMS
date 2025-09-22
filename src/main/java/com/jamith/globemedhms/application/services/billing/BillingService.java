package com.jamith.globemedhms.application.services.billing;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;

import java.util.List;

public interface BillingService {
    List<Billing> getAllBillings();
    Billing saveOrUpdateBilling(Billing billing);
    Billing getBillingById(int id);
    Billing generateBill(Appointment appointment, double amount, String billingType);
    Billing getBillingByAppointment(int appointmentId);
    void updateBillingStatus(int billingId, String status);

    List<Object[]> getMonthlyRevenue();
    List<Object[]> getBillingStatusDistribution();
    List<Object[]> getBillingTypeDistribution();
    double getTotalRevenue();
    long getTotalBillingCount();
    long getInsuranceBillingCount();
}