// ./src/main/java/com/jamith/globemedhms/presentation/controllers/PaymentController.java
package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.billing.PaymentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentController {
    private final PaymentView view;
    private final BillingService billingService = new BillingServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy();

    public PaymentController(PaymentView view, Staff loggedInStaff) {
        this.view = view;
        view.addProcessCashPaymentListener(new ProcessCashPaymentListener(loggedInStaff));
        view.addProcessInsurancePaymentListener(new ProcessInsurancePaymentListener(loggedInStaff));
    }

    class ProcessCashPaymentListener implements ActionListener {
        private final Staff staff;

        public ProcessCashPaymentListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "BILLING", "MANAGE_BILLING");
                Billing selectedBill = view.getSelectedPendingBill();
                if (selectedBill != null) {
                    billingService.processCashPayment(selectedBill.getId());
                    view.showMessage("Cash payment processed successfully for Bill ID: " + selectedBill.getId());
                    view.refreshAllLists();
                } else {
                    view.showMessage("Please select a pending bill to process.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            } catch (IllegalStateException ex) {
                view.showMessage("Error: " + ex.getMessage());
            }
        }
    }

    class ProcessInsurancePaymentListener implements ActionListener {
        private final Staff staff;

        public ProcessInsurancePaymentListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "INSURANCE_CLAIMS", "PROCESS_CLAIMS");
                Billing selectedBill = view.getSelectedPendingInsuranceBill();
                if (selectedBill != null) {
                    billingService.processInsurancePayment(selectedBill.getId());
                    view.showMessage("Insurance payment processed successfully for Bill ID: " + selectedBill.getId());
                    view.refreshAllLists();
                } else {
                    view.showMessage("Please select a pending insurance bill to process.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            } catch (IllegalStateException ex) {
                view.showMessage("Error: " + ex.getMessage());
            }
        }
    }
}