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

public class ViewPrescriptionsView extends JPanel {
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private JList<Appointment> appointmentList;
    private JTextArea prescriptionArea;
    private JButton refreshButton;

    public ViewPrescriptionsView(Staff loggedInStaff) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        appointmentList = new JList<>();
        updateAppointmentList();
        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        add(appointmentListScroll, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Prescription Details"));
        prescriptionArea = new JTextArea(5, 20);
        prescriptionArea.setEditable(false);
        detailsPanel.add(new JScrollPane(prescriptionArea));
        refreshButton = new JButton("Refresh");
        detailsPanel.add(refreshButton);
        add(detailsPanel, BorderLayout.CENTER);

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

    public void addRefreshListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}