package com.jamith.globemedhms.application.services.billing;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import com.jamith.globemedhms.infrastructure.repository.BillingRepository;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

public class BillingServiceImpl implements BillingService {
    private static final Logger logger = LogManager.getLogger(BillingServiceImpl.class);
    private final BillingRepository billingRepository = new BillingRepository();

    @Override
    public List<Billing> getAllBillings() {
        return billingRepository.getAllBillings();
    }

    @Override
    public void saveOrUpdateBilling(Billing billing) {
        billingRepository.saveOrUpdate(billing);
    }

    @Override
    public Billing getBillingById(int id) {
        return billingRepository.findById(id);
    }

    @Override
    public Billing generateBill(Appointment appointment, double amount) {
        Billing billing = new Billing(appointment, amount, "PENDING");
        saveOrUpdateBilling(billing);
        logger.info("Generated bill for appointment ID: {}", appointment.getId());
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
}