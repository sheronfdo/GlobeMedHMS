package com.jamith.globemedhms.presentation.views.appointment;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.application.services.staff.StaffService;
import com.jamith.globemedhms.application.services.staff.StaffServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.controllers.AppointmentController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class AppointmentView extends JPanel {
    private final AppointmentService service = new AppointmentServiceImpl();
    private final PatientService patientService = new PatientServiceImpl();
    private final StaffService staffService = new StaffServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy();
    private JList<Appointment> appointmentList;
    private JComboBox<Staff> staffComboBox;
    private JComboBox<Patient> patientComboBox;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> typeComboBox;
    private JTextArea treatmentArea;
    private JTextArea prescriptionArea;
    private JButton bookButton;
    private JButton cancelButton;
    private JButton completeButton;
    private final Staff loggedInStaff;

    public AppointmentView(Staff loggedInStaff) {
        this.loggedInStaff = loggedInStaff;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Appointment list with scroll pane
        appointmentList = new JList<>();
        updateAppointmentList(loggedInStaff);
        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        add(appointmentListScroll, BorderLayout.WEST);

        // Right panel for appointment details (GridLayout)
        JPanel detailsPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));
        detailsPanel.add(new JLabel("Staff:"));
        staffComboBox = new JComboBox<>(staffService.getAllStaff().toArray(new Staff[0]));
        detailsPanel.add(staffComboBox);
        detailsPanel.add(new JLabel("Patient:"));
        patientComboBox = new JComboBox<>(patientService.getAllPatients().toArray(new Patient[0]));
        detailsPanel.add(patientComboBox);
        detailsPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(20);
        detailsPanel.add(dateField);
        detailsPanel.add(new JLabel("Time (HH:MM):"));
        timeField = new JTextField(20);
        detailsPanel.add(timeField);
        detailsPanel.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"CONSULTATION", "DIAGNOSTICS", "SURGERY"});
        detailsPanel.add(typeComboBox);
        detailsPanel.add(new JLabel("Treatment Details:"));
        treatmentArea = new JTextArea(5, 20);
        detailsPanel.add(new JScrollPane(treatmentArea));
        detailsPanel.add(new JLabel("Prescription:"));
        prescriptionArea = new JTextArea(5, 20);
        prescriptionArea.setEnabled(loggedInStaff.hasPermission("PRESCRIBE_MEDICATIONS"));
        detailsPanel.add(new JScrollPane(prescriptionArea));
        bookButton = new JButton("Book Appointment");
        detailsPanel.add(bookButton);
        completeButton = new JButton("Complete Appointment");
        completeButton.setEnabled(loggedInStaff.hasPermission("PRESCRIBE_MEDICATIONS"));
        detailsPanel.add(completeButton);
        add(detailsPanel, BorderLayout.CENTER);

        cancelButton = new JButton("Cancel Appointment");
        add(cancelButton, BorderLayout.SOUTH);

        // Listener for selecting appointment from list
        appointmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedAppointment = appointmentList.getSelectedValue();
                if (selectedAppointment != null) {
                    staffComboBox.setSelectedItem(selectedAppointment.getStaff());
                    patientComboBox.setSelectedItem(selectedAppointment.getPatient());
                    dateField.setText(selectedAppointment.getDate());
                    timeField.setText(selectedAppointment.getTime());
                    typeComboBox.setSelectedItem(selectedAppointment.getType());
                    treatmentArea.setText(selectedAppointment.getTreatmentDetails());
                    prescriptionArea.setText(selectedAppointment.getPrescription());
                }
            }
        });

        new AppointmentController(this, loggedInStaff);
    }

    public void updateAppointmentList(Staff staff) {
        List<Appointment> appointments = service.getAppointmentsByStaff(staff.getId());
        appointmentList.setListData(appointments.toArray(new Appointment[0]));
    }

    public Appointment getSelectedAppointment() {
        return appointmentList.getSelectedValue();
    }

    public Staff getSelectedStaff() {
        return (Staff) staffComboBox.getSelectedItem();
    }

    public Patient getSelectedPatient() {
        return (Patient) patientComboBox.getSelectedItem();
    }

    public String getDate() {
        return dateField.getText();
    }

    public String getTime() {
        return timeField.getText();
    }

    public String getType() {
        return (String) typeComboBox.getSelectedItem();
    }

    public String getTreatmentDetails() {
        return treatmentArea.getText();
    }

    public String getPrescription() {
        return prescriptionArea.getText();
    }

    public void addBookListener(ActionListener listener) {
        bookButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void addCompleteListener(ActionListener listener) {
        completeButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}