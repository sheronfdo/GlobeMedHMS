package com.jamith.globemedhms.presentation.views.billing;

import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.presentation.controllers.PaymentController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class PaymentView extends JPanel {
    private final BillingService billingService = new BillingServiceImpl();
    private JList<Billing> pendingCashBillsList;
    private JList<Billing> pendingInsuranceBillsList;
    private JButton processCashPaymentButton;
    private JButton processInsurancePaymentButton;
    private JTabbedPane tabbedPane;
    private JLabel cashSummaryLabel;
    private JLabel insuranceSummaryLabel;

    public PaymentView(Staff loggedInStaff) {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JLabel headerLabel = new JLabel("💳 Payment Processing Center", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(new Color(0, 100, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Create tabbed pane for different bill types
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tab 1: Pending Cash Payments
        JPanel cashPanel = createCashPaymentPanel();
        tabbedPane.addTab("💰 Cash Payments", cashPanel);

        // Tab 2: Pending Insurance Payments
        JPanel insurancePanel = createInsurancePaymentPanel();
        tabbedPane.addTab("🏥 Insurance Payments", insurancePanel);

        add(tabbedPane, BorderLayout.CENTER);

        new PaymentController(this, loggedInStaff);
    }

    private JPanel createCashPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Summary label
        cashSummaryLabel = new JLabel("Loading pending cash payments...");
        cashSummaryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        cashSummaryLabel.setBorder(new EmptyBorder(0, 10, 5, 0));
        panel.add(cashSummaryLabel, BorderLayout.NORTH);

        // Bills list with custom renderer
        pendingCashBillsList = new JList<>();
        pendingCashBillsList.setCellRenderer(new BillingListCellRenderer());
        updatePendingCashBillsList();
        pendingCashBillsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendingCashBillsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane listScroll = new JScrollPane(pendingCashBillsList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 179, 113), 2),
                "Pending Cash Bills",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(60, 179, 113)
        ));

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> updatePendingCashBillsList());

        processCashPaymentButton = new JButton("💵 Process Cash Payment");
        processCashPaymentButton.setBackground(new Color(60, 179, 113));
        processCashPaymentButton.setForeground(Color.WHITE);
        processCashPaymentButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        processCashPaymentButton.setFocusPainted(false);

        buttonPanel.add(refreshButton);
        buttonPanel.add(processCashPaymentButton);

        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInsurancePaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Summary label
        insuranceSummaryLabel = new JLabel("Loading pending insurance payments...");
        insuranceSummaryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        insuranceSummaryLabel.setBorder(new EmptyBorder(0, 10, 5, 0));
        panel.add(insuranceSummaryLabel, BorderLayout.NORTH);

        // Bills list with custom renderer
        pendingInsuranceBillsList = new JList<>();
        pendingInsuranceBillsList.setCellRenderer(new BillingListCellRenderer());
        updatePendingInsuranceBillsList();
        pendingInsuranceBillsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendingInsuranceBillsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane listScroll = new JScrollPane(pendingInsuranceBillsList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Pending Insurance Bills",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(70, 130, 180)
        ));

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> updatePendingInsuranceBillsList());

        processInsurancePaymentButton = new JButton("🏥 Process Insurance Payment");
        processInsurancePaymentButton.setBackground(new Color(70, 130, 180));
        processInsurancePaymentButton.setForeground(Color.WHITE);
        processInsurancePaymentButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        processInsurancePaymentButton.setFocusPainted(false);

        buttonPanel.add(refreshButton);
        buttonPanel.add(processInsurancePaymentButton);

        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Custom cell renderer for better bill display
    private class BillingListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Billing) {
                Billing bill = (Billing) value;
                String patientName = bill.getAppointment().getPatient().getName();
                String appointmentDate = bill.getAppointment().getDate();
                double amount = bill.getAmount();
                String status = bill.getStatus();

                // Create a descriptive display text
                String displayText = String.format(
                        "<html><b>Bill #%d</b> - %s<br>" +
                                "Appointment: %s | Amount: $%.2f<br>" +
                                "Status: <font color='%s'>%s</font></html>",
                        bill.getId(),
                        patientName,
                        appointmentDate,
                        amount,
                        getStatusColor(status),
                        status
                );

                setText(displayText);

                // Set tooltip with full details
                setToolTipText(String.format(
                        "Bill ID: %d | Patient: %s | Date: %s | Amount: $%.2f | Type: %s",
                        bill.getId(), patientName, appointmentDate, amount, bill.getBillingType()
                ));
            }

            if (isSelected) {
                setBackground(new Color(220, 240, 255));
                setForeground(Color.BLACK);
            } else {
                setBackground(index % 2 == 0 ? new Color(250, 250, 250) : new Color(245, 245, 245));
            }

            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return this;
        }

        private String getStatusColor(String status) {
            switch (status) {
                case "PENDING": return "orange";
                case "INSURANCE_PENDING": return "blue";
                case "PAID": return "green";
                case "INSURANCE_PAID": return "darkgreen";
                default: return "black";
            }
        }
    }

    public void updatePendingCashBillsList() {
        List<Billing> pendingBills = billingService.getPendingBillings();
        pendingCashBillsList.setListData(pendingBills.toArray(new Billing[0]));

        // Update summary
        double totalAmount = pendingBills.stream().mapToDouble(Billing::getAmount).sum();
        cashSummaryLabel.setText(String.format(
                "📊 %d pending cash bill(s) | Total amount: $%.2f",
                pendingBills.size(), totalAmount
        ));
        cashSummaryLabel.setForeground(pendingBills.isEmpty() ? Color.GRAY : new Color(60, 179, 113));
    }

    public void updatePendingInsuranceBillsList() {
        List<Billing> pendingInsuranceBills = billingService.getPendingInsuranceBillings();
        pendingInsuranceBillsList.setListData(pendingInsuranceBills.toArray(new Billing[0]));

        // Update summary
        double totalAmount = pendingInsuranceBills.stream().mapToDouble(Billing::getAmount).sum();
        insuranceSummaryLabel.setText(String.format(
                "📊 %d pending insurance bill(s) | Total amount: $%.2f",
                pendingInsuranceBills.size(), totalAmount
        ));
        insuranceSummaryLabel.setForeground(pendingInsuranceBills.isEmpty() ? Color.GRAY : new Color(70, 130, 180));
    }

    public Billing getSelectedPendingCashBill() {
        return pendingCashBillsList.getSelectedValue();
    }

    public Billing getSelectedPendingInsuranceBill() {
        return pendingInsuranceBillsList.getSelectedValue();
    }

    public void addProcessCashPaymentListener(java.awt.event.ActionListener listener) {
        processCashPaymentButton.addActionListener(listener);
    }

    public void addProcessInsurancePaymentListener(java.awt.event.ActionListener listener) {
        processInsurancePaymentButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void refreshAllLists() {
        updatePendingCashBillsList();
        updatePendingInsuranceBillsList();
    }

    public void showPaymentConfirmation(Billing bill, String paymentType) {
        String message = String.format(
                "<html><div style='text-align: center;'>" +
                        "<h3 style='color: green;'>✅ Payment Processed Successfully!</h3>" +
                        "<b>Bill #%d</b><br>" +
                        "Patient: %s<br>" +
                        "Amount: <b>$%.2f</b><br>" +
                        "Payment Type: %s<br>" +
                        "Date: %s" +
                        "</div></html>",
                bill.getId(),
                bill.getAppointment().getPatient().getName(),
                bill.getAmount(),
                paymentType,
                java.time.LocalDate.now()
        );

        JOptionPane.showMessageDialog(this, message, "Payment Confirmation",
                JOptionPane.INFORMATION_MESSAGE);
    }
}