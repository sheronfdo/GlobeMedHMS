package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.billing.PaymentView;

import javax.swing.*;
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
                Billing selectedBill = view.getSelectedPendingCashBill();
                if (selectedBill != null) {
                    // Confirm payment
                    int confirm = JOptionPane.showConfirmDialog(
                            view,
                            String.format(
                                    "<html>Process <b>cash payment</b> for:<br><br>" +
                                            "Bill #%d - %s<br>" +
                                            "Amount: <b>$%.2f</b><br><br>" +
                                            "Confirm payment?</html>",
                                    selectedBill.getId(),
                                    selectedBill.getAppointment().getPatient().getName(),
                                    selectedBill.getAmount()
                            ),
                            "Confirm Cash Payment",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        Billing processedBill = billingService.processCashPayment(selectedBill.getId());
                        view.showPaymentConfirmation(processedBill, "Cash");
                        view.refreshAllLists();
                    }
                } else {
                    view.showMessage("💡 Please select a pending cash bill to process.");
                }
            } catch (SecurityException ex) {
                view.showMessage("🔒 Access Denied: " + ex.getMessage());
            } catch (IllegalStateException ex) {
                view.showMessage("❌ Error: " + ex.getMessage());
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
                    // Confirm payment
                    int confirm = JOptionPane.showConfirmDialog(
                            view,
                            String.format(
                                    "<html>Process <b>insurance payment</b> for:<br><br>" +
                                            "Bill #%d - %s<br>" +
                                            "Amount: <b>$%.2f</b><br>" +
                                            "Insurance Provider: %s<br><br>" +
                                            "Confirm insurance payment processing?</html>",
                                    selectedBill.getId(),
                                    selectedBill.getAppointment().getPatient().getName(),
                                    selectedBill.getAmount(),
                                    getInsuranceProvider(selectedBill)
                            ),
                            "Confirm Insurance Payment",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        Billing processedBill = billingService.processInsurancePayment(selectedBill.getId());
                        view.showPaymentConfirmation(processedBill, "Insurance");
                        view.refreshAllLists();
                    }
                } else {
                    view.showMessage("💡 Please select a pending insurance bill to process.");
                }
            } catch (SecurityException ex) {
                view.showMessage("🔒 Access Denied: " + ex.getMessage());
            } catch (IllegalStateException ex) {
                view.showMessage("❌ Error: " + ex.getMessage());
            }
        }

        private String getInsuranceProvider(Billing bill) {
            // This would need integration with InsuranceClaim service
            // For now, return a placeholder
            return "Insurance Provider";
        }
    }
}