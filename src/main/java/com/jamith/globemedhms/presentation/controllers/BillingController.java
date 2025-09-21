package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.application.services.insurance.InsuranceClaimService;
import com.jamith.globemedhms.application.services.insurance.InsuranceClaimServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.InsuranceClaim;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.billing.BillingView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class BillingController {
    private final BillingView view;
    private final BillingService billingService = new BillingServiceImpl();
    private final InsuranceClaimService claimService = new InsuranceClaimServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy();

    public BillingController(BillingView view, Staff loggedInStaff) {
        this.view = view;
        view.addGenerateBillListener(new GenerateBillListener(loggedInStaff));
        view.addProcessClaimListener(new ProcessClaimListener(loggedInStaff));
    }

    class GenerateBillListener implements ActionListener {
        private final Staff staff;

        public GenerateBillListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "BILLING", "MANAGE_BILLING");
                Appointment selectedAppointment = view.getSelectedAppointment();
                if (selectedAppointment != null) {
                    double amount = view.getAmount();
                    String billingType = view.getBillingType();

                    if (amount > 0) {
                        // Validate insurance information if billing type is INSURANCE
                        if ("INSURANCE".equals(billingType)) {
                            String insuranceProvider = view.getInsuranceProvider();
                            String policyNumber = view.getPolicyNumber();

                            if (insuranceProvider == null || insuranceProvider.trim().isEmpty()) {
                                view.showMessage("Please enter insurance provider for insurance billing.");
                                return;
                            }

                            if (policyNumber == null || policyNumber.trim().isEmpty()) {
                                view.showMessage("Please enter policy number for insurance billing.");
                                return;
                            }
                        }

                        Billing billing = billingService.generateBill(selectedAppointment, amount, billingType);

                        // For insurance billing, create claim with provider and policy info
                        if ("INSURANCE".equals(billingType)) {
                            String insuranceProvider = view.getInsuranceProvider();
                            String policyNumber = view.getPolicyNumber();

                            InsuranceClaim claim = claimService.createClaim(
                                    billing,
                                    insuranceProvider,
                                    policyNumber
                            );

                            view.showMessage("Insurance bill generated successfully for appointment ID: " +
                                    selectedAppointment.getId() + ". Claim ID: " + claim.getId());
                        } else {
                            view.showMessage("Direct bill generated successfully for appointment ID: " +
                                    selectedAppointment.getId());
                        }

                        view.enableProcessClaimButton("INSURANCE".equals(billingType));
                    } else {
                        view.showMessage("Please enter a valid amount.");
                    }
                } else {
                    view.showMessage("Please select an appointment.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            } catch (IllegalArgumentException ex) {
                view.showMessage("Error: " + ex.getMessage());
            }
        }
    }

    class ProcessClaimListener implements ActionListener {
        private final Staff staff;

        public ProcessClaimListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "INSURANCE_CLAIMS", "PROCESS_CLAIMS");
                Appointment selectedAppointment = view.getSelectedAppointment();
                if (selectedAppointment != null) {
                    Billing billing = billingService.getBillingByAppointment(selectedAppointment.getId());
                    if (billing != null) {
                        InsuranceClaim claim = claimService.getClaimByBillingId(billing.getId());
                        if (claim != null) {
                            claim = claimService.processClaim(claim);
                            view.showMessage("Claim processed successfully. Status: " + claim.getStatus());
                        } else {
                            view.showMessage("No insurance claim found for this billing.");
                        }
                    } else {
                        view.showMessage("No billing found for the selected appointment.");
                    }
                } else {
                    view.showMessage("Please select an appointment.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }
}