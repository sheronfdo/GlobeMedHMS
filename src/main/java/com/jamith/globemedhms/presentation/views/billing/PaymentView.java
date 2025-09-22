// ./src/main/java/com/jamith/globemedhms/presentation/views/billing/PaymentView.java
package com.jamith.globemedhms.presentation.views.billing;

import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.presentation.controllers.PaymentController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class PaymentView extends JPanel {
    private final BillingService billingService = new BillingServiceImpl();
    private JList<Billing> pendingBillsList;
    private JList<Billing> pendingInsuranceBillsList;
    private JButton processCashPaymentButton;
    private JButton processInsurancePaymentButton;
    private JTabbedPane tabbedPane;

    public PaymentView(Staff loggedInStaff) {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Create tabbed pane for different bill types
        tabbedPane = new JTabbedPane();

        // Tab 1: Pending Cash Payments
        JPanel cashPanel = createCashPaymentPanel();
        tabbedPane.addTab("💰 Pending Cash Payments", cashPanel);

        // Tab 2: Pending Insurance Payments
        JPanel insurancePanel = createInsurancePaymentPanel();
        tabbedPane.addTab("🏥 Pending Insurance Payments", insurancePanel);

        add(tabbedPane, BorderLayout.CENTER);

        new PaymentController(this, loggedInStaff);
    }

    private JPanel createCashPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        pendingBillsList = new JList<>();
        updatePendingBillsList();
        pendingBillsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendingBillsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane listScroll = new JScrollPane(pendingBillsList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Pending Cash Bills"));
        
        processCashPaymentButton = new JButton("💵 Process Cash Payment");
        processCashPaymentButton.setBackground(new Color(60, 179, 113));
        processCashPaymentButton.setForeground(Color.WHITE);
        processCashPaymentButton.setFocusPainted(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(processCashPaymentButton);
        
        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createInsurancePaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        pendingInsuranceBillsList = new JList<>();
        updatePendingInsuranceBillsList();
        pendingInsuranceBillsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendingInsuranceBillsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane listScroll = new JScrollPane(pendingInsuranceBillsList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Pending Insurance Bills"));
        
        processInsurancePaymentButton = new JButton("🏥 Process Insurance Payment");
        processInsurancePaymentButton.setBackground(new Color(70, 130, 180));
        processInsurancePaymentButton.setForeground(Color.WHITE);
        processInsurancePaymentButton.setFocusPainted(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(processInsurancePaymentButton);
        
        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    public void updatePendingBillsList() {
        List<Billing> pendingBills = billingService.getPendingBillings();
        pendingBillsList.setListData(pendingBills.toArray(new Billing[0]));
    }

    public void updatePendingInsuranceBillsList() {
        List<Billing> pendingInsuranceBills = billingService.getPendingInsuranceBillings();
        pendingInsuranceBillsList.setListData(pendingInsuranceBills.toArray(new Billing[0]));
    }

    public Billing getSelectedPendingBill() {
        return pendingBillsList.getSelectedValue();
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
        updatePendingBillsList();
        updatePendingInsuranceBillsList();
    }
}