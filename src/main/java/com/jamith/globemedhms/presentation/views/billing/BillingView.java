package com.jamith.globemedhms.presentation.views.billing;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.application.services.insurance.InsuranceClaimService;
import com.jamith.globemedhms.application.services.insurance.InsuranceClaimServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.InsuranceClaim;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.presentation.controllers.BillingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class BillingView extends JPanel {
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private final BillingService billingService = new BillingServiceImpl();
    private final InsuranceClaimService claimService = new InsuranceClaimServiceImpl();
    private JList<Appointment> appointmentList;
    private JTextField amountField;
    private JComboBox<String> billingTypeComboBox;
    private JButton generateBillButton;
    private JButton processClaimButton;
    private final Staff loggedInStaff;

    public BillingView(Staff loggedInStaff) {
        this.loggedInStaff = loggedInStaff;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        appointmentList = new JList<>();
        updateAppointmentList();
        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        add(appointmentListScroll, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Billing and Claims"));
        detailsPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(20);
        detailsPanel.add(amountField);
        detailsPanel.add(new JLabel("Billing Type:"));
        billingTypeComboBox = new JComboBox<>(new String[]{"DIRECT", "INSURANCE"});
        detailsPanel.add(billingTypeComboBox);
        generateBillButton = new JButton("Generate Bill");
        detailsPanel.add(generateBillButton);
        processClaimButton = new JButton("Process Insurance Claim");
        processClaimButton.setEnabled(false);
        detailsPanel.add(processClaimButton);
        add(detailsPanel, BorderLayout.CENTER);

        new BillingController(this, loggedInStaff);
    }

    public void updateAppointmentList() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        appointmentList.setListData(appointments.toArray(new Appointment[0]));
    }

    public Appointment getSelectedAppointment() {
        return appointmentList.getSelectedValue();
    }

    public double getAmount() {
        try {
            return Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getBillingType() {
        return (String) billingTypeComboBox.getSelectedItem();
    }

    public void addGenerateBillListener(ActionListener listener) {
        generateBillButton.addActionListener(listener);
    }

    public void addProcessClaimListener(ActionListener listener) {
        processClaimButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void enableProcessClaimButton(boolean enabled) {
        processClaimButton.setEnabled(enabled);
    }
}