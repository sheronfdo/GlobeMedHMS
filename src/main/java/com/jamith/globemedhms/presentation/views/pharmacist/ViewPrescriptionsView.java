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

public class ViewPrescriptionsView extends JPanel {
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private JList<Appointment> appointmentList;
    private JTextArea prescriptionArea;
    private JButton refreshButton;

    public ViewPrescriptionsView(Staff loggedInStaff) {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left Panel: Appointment List
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Appointments"));

        appointmentList = new JList<>();
        updateAppointmentList();
        appointmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentList.setVisibleRowCount(12);

        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        leftPanel.add(appointmentListScroll, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // Center Panel: Prescription Details
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Prescription Details"));

        prescriptionArea = new JTextArea(10, 25);
        prescriptionArea.setEditable(false);
        prescriptionArea.setLineWrap(true);
        prescriptionArea.setWrapStyleWord(true);

        JScrollPane prescriptionScroll = new JScrollPane(prescriptionArea);
        prescriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        detailsPanel.add(prescriptionScroll, BorderLayout.CENTER);

        // Refresh Button
        refreshButton = new JButton("🔄 Refresh Prescriptions");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(200, 35));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);

        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(detailsPanel, BorderLayout.CENTER);

        // List selection listener
        appointmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedAppointment = appointmentList.getSelectedValue();
                if (selectedAppointment != null) {
                    prescriptionArea.setText(EncryptionDecorator.decrypt(selectedAppointment.getPrescription()));
                }
            }
        });

        // Controller hookup
        new PharmacistController(this, loggedInStaff);

    }

    public void updateAppointmentList() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        appointmentList.setListData(appointments.toArray(new Appointment[0]));
    }

    public Appointment getSelectedAppointment() {
        return appointmentList.getSelectedValue();
    }

    public void addRefreshListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}