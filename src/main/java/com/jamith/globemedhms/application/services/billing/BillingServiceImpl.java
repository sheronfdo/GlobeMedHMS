package com.jamith.globemedhms.application.services.billing;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.infrastructure.repository.BillingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BillingServiceImpl implements BillingService {
    private static final Logger logger = LogManager.getLogger(BillingServiceImpl.class);
    private final BillingRepository billingRepository = new BillingRepository();

    @Override
    public List<Billing> getAllBillings() {
        return billingRepository.getAllBillings();
    }

    @Override
    public Billing saveOrUpdateBilling(Billing billing) {
        return billingRepository.saveOrUpdate(billing);
    }

    @Override
    public Billing getBillingById(int id) {
        return billingRepository.findById(id);
    }

    @Override
    public Billing generateBill(Appointment appointment, double amount, String billingType) {
        String status = "INSURANCE".equals(billingType) ? "INSURANCE_PENDING" : "PENDING";
        Billing billing = new Billing(appointment, amount, status, billingType);
        billing = saveOrUpdateBilling(billing);
        logger.info("Generated {} bill for appointment ID: {}", billingType, appointment.getId());
        return billing;
    }

    @Override
    public Billing getBillingByAppointment(int appointmentId) {
        Billing billing = billingRepository.findByAppointmentId(appointmentId);
        if (billing != null) {
            logger.info("Found billing for appointment ID: {}", appointmentId);
        } else {
            logger.warn("No billing found for appointment ID: {}", appointmentId);
        }
        return billing;
    }

    @Override
    public void updateBillingStatus(int billingId, String status) {
        Billing billing = getBillingById(billingId);
        if (billing != null) {
            billing.setStatus(status);
            saveOrUpdateBilling(billing);
            logger.info("Updated billing ID {} status to: {}", billingId, status);
        }
    }

    @Override
    public Billing processCashPayment(int billingId) {
        Billing billing = getBillingById(billingId);
        if (billing != null && "PENDING".equals(billing.getStatus())) {
            billing.setStatus("PAID");
            billing.setPaymentDate(java.time.LocalDate.now().toString());
            billing.setPaymentMethod("CASH");
            billing = saveOrUpdateBilling(billing);
            logger.info("💰 Processed CASH payment for Bill #{} - Patient: {}",
                    billingId, billing.getAppointment().getPatient().getName());
            return billing;
        } else {
            String errorMsg = String.format("Cannot process cash payment for Bill #%d. Current status: %s",
                    billingId, billing != null ? billing.getStatus() : "NOT_FOUND");
            logger.warn(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    @Override
    public Billing processInsurancePayment(int billingId) {
        Billing billing = getBillingById(billingId);
        if (billing != null && "INSURANCE_PENDING".equals(billing.getStatus())) {
            billing.setStatus("INSURANCE_PAID");
            billing.setPaymentDate(java.time.LocalDate.now().toString());
            billing.setPaymentMethod("INSURANCE");
            billing = saveOrUpdateBilling(billing);
            logger.info("🏥 Processed INSURANCE payment for Bill #{} - Patient: {}",
                    billingId, billing.getAppointment().getPatient().getName());
            return billing;
        } else {
            String errorMsg = String.format("Cannot process insurance payment for Bill #%d. Current status: %s",
                    billingId, billing != null ? billing.getStatus() : "NOT_FOUND");
            logger.warn(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    @Override
    public List<Billing> getPendingBillings() {
        return billingRepository.findByStatus("PENDING");
    }

    @Override
    public List<Billing> getPendingInsuranceBillings() {
        return billingRepository.findByStatus("INSURANCE_PENDING");
    }

    @Override
    public List<Object[]> getMonthlyRevenue() {
        return billingRepository.getMonthlyRevenue();
    }

    @Override
    public List<Object[]> getBillingStatusDistribution() {
        return billingRepository.getBillingStatusDistribution();
    }

    @Override
    public List<Object[]> getBillingTypeDistribution() {
        return billingRepository.getBillingTypeDistribution();
    }

    @Override
    public double getTotalRevenue() {
       return billingRepository.getTotalRevenue();
    }

    @Override
    public long getTotalBillingCount() {
        return billingRepository.getTotalBillingCount();
    }

    @Override
    public long getInsuranceBillingCount() {
        return billingRepository.getInsuranceBillingCount();
    }
}