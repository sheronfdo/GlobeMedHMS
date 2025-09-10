package com.jamith.globemedhms.presentation.views.pharmacist;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.presentation.controllers.PharmacistController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class DispenseMedView extends JPanel {
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private JList<Appointment> appointmentList;
    private JTextArea prescriptionArea;
    private JButton dispenseButton;

    public DispenseMedView(Staff loggedInStaff) {
//        setLayout(new BorderLayout(10, 10));
//        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        appointmentList = new JList<>();
//        updateAppointmentList();
//        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
//        add(appointmentListScroll, BorderLayout.WEST);
//
//        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
//        detailsPanel.setBorder(BorderFactory.createTitledBorder("Dispense Medications"));
//        prescriptionArea = new JTextArea(5, 20);
//        prescriptionArea.setEditable(false);
//        detailsPanel.add(new JScrollPane(prescriptionArea));
//        dispenseButton = new JButton("Dispense and Record");
//        detailsPanel.add(dispenseButton);
//        add(detailsPanel, BorderLayout.CENTER);
//
//        appointmentList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                Appointment selectedAppointment = appointmentList.getSelectedValue();
//                if (selectedAppointment != null) {
//                    prescriptionArea.setText(selectedAppointment.getPrescription());
//                }
//            }
//        });
//
//        new PharmacistController(this, loggedInStaff);


        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left: Appointment List
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Appointments"));

        appointmentList = new JList<>();
        updateAppointmentList();
        appointmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentList.setVisibleRowCount(12);
        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        leftPanel.add(appointmentListScroll, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // Center: Prescription + Dispense Button
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Dispense Medications"));

        prescriptionArea = new JTextArea(8, 25);
        prescriptionArea.setEditable(false);
        prescriptionArea.setLineWrap(true);
        prescriptionArea.setWrapStyleWord(true);
        JScrollPane prescriptionScroll = new JScrollPane(prescriptionArea);
        prescriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsPanel.add(prescriptionScroll, BorderLayout.CENTER);

        dispenseButton = new JButton("✅ Dispense & Record");
        dispenseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dispenseButton.setBackground(new Color(72, 201, 176));
        dispenseButton.setForeground(Color.WHITE);
        dispenseButton.setFocusPainted(false);
        dispenseButton.setPreferredSize(new Dimension(200, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(dispenseButton);

        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(detailsPanel, BorderLayout.CENTER);

        // Listener: Load prescriptions on selection
        appointmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedAppointment = appointmentList.getSelectedValue();
                if (selectedAppointment != null) {
                    prescriptionArea.setText(EncryptionDecorator.decrypt(selectedAppointment.getPrescription()));
                }
            }
        });

        new PharmacistController(this, loggedInStaff);
    }

    public void updateAppointmentList() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        appointmentList.setListData(appointments.toArray(new Appointment[0]));
    }

    public Appointment getSelectedAppointment() {
        return appointmentList.getSelectedValue();
    }

    public void addDispenseListener(ActionListener listener) {
        dispenseButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}