package com.jamith.globemedhms.presentation.views.pharmacist;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Staff;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Appointment list with prescriptions
        appointmentList = new JList<>();
        updateAppointmentList();
        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        add(appointmentListScroll, BorderLayout.WEST);

        // Right panel for dispensation
        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Dispense Medications"));
        prescriptionArea = new JTextArea(5, 20);
        prescriptionArea.setEditable(false);
        detailsPanel.add(new JScrollPane(prescriptionArea));
        dispenseButton = new JButton("Dispense and Record");
        detailsPanel.add(dispenseButton);
        add(detailsPanel, BorderLayout.CENTER);

        // Listener for selecting appointment
        appointmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedAppointment = appointmentList.getSelectedValue();
                if (selectedAppointment != null) {
                    prescriptionArea.setText(selectedAppointment.getPrescription());
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