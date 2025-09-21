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
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.controllers.AppointmentController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
    private JButton undoButton;
    private final Staff loggedInStaff;

    public AppointmentView(Staff loggedInStaff) {
        this.loggedInStaff = loggedInStaff;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Left panel: Appointment list
        appointmentList = new JList<>();
        updateAppointmentList(loggedInStaff);
        appointmentList.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        appointmentList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appointmentList.setSelectionBackground(new Color(100, 149, 237));
        JScrollPane appointmentListScroll = new JScrollPane(appointmentList);
        appointmentListScroll.setBorder(BorderFactory.createTitledBorder("Appointments"));
        appointmentListScroll.setPreferredSize(new Dimension(220, 0));
        add(appointmentListScroll, BorderLayout.WEST);

        // Center panel: Details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Staff
        detailsPanel.add(new JLabel("Staff:"), gbc);
        gbc.gridx++;
        staffComboBox = new JComboBox<>(staffService.getAllStaff().toArray(new Staff[0]));
        staffComboBox.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        detailsPanel.add(staffComboBox, gbc);

        // Patient
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx++;
        patientComboBox = new JComboBox<>(patientService.getAllPatients().toArray(new Patient[0]));
        patientComboBox.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        detailsPanel.add(patientComboBox, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx++;
        dateField = new JTextField(15);
        dateField.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        detailsPanel.add(dateField, gbc);

        // Time
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Time (HH:MM):"), gbc);
        gbc.gridx++;
        timeField = new JTextField(15);
        timeField.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        detailsPanel.add(timeField, gbc);

        // Type
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx++;
        typeComboBox = new JComboBox<>(new String[]{"CONSULTATION", "DIAGNOSTICS", "SURGERY"});
        typeComboBox.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        detailsPanel.add(typeComboBox, gbc);

        // Treatment
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Treatment Details:"), gbc);
        gbc.gridx++;
        treatmentArea = new JTextArea(4, 15);
        treatmentArea.setLineWrap(true);
        treatmentArea.setWrapStyleWord(true);
        treatmentArea.setBorder(new LineBorder(Color.GRAY, 1));
        treatmentArea.setEnabled(loggedInStaff.hasPermission("PRESCRIBE_MEDICATIONS"));
        detailsPanel.add(new JScrollPane(treatmentArea), gbc);

        // Prescription
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Prescription:"), gbc);
        gbc.gridx++;
        prescriptionArea = new JTextArea(4, 15);
        prescriptionArea.setLineWrap(true);
        prescriptionArea.setWrapStyleWord(true);
        prescriptionArea.setBorder(new LineBorder(Color.GRAY, 1));
        prescriptionArea.setEnabled(loggedInStaff.hasPermission("PRESCRIBE_MEDICATIONS"));
        detailsPanel.add(new JScrollPane(prescriptionArea), gbc);

        // Action buttons (book + complete)
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bookButton = new JButton("Book Appointment");
        bookButton.setBackground(new Color(60, 179, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        actionPanel.add(bookButton);

        completeButton = new JButton("Complete Appointment");
        completeButton.setBackground(new Color(100, 149, 237));
        completeButton.setForeground(Color.WHITE);
        completeButton.setFocusPainted(false);
        completeButton.setEnabled(loggedInStaff.hasPermission("PRESCRIBE_MEDICATIONS"));
        actionPanel.add(completeButton);

        detailsPanel.add(actionPanel, gbc);
        add(detailsPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cancelButton = new JButton("Cancel Appointment");
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        buttonPanel.add(cancelButton);

        undoButton = new JButton("Undo");
        undoButton.setBackground(new Color(255, 165, 0));
        undoButton.setForeground(Color.WHITE);
        undoButton.setFocusPainted(false);
        undoButton.setEnabled(loggedInStaff.hasPermission("UPDATE_PATIENT_RECORDS"));
        buttonPanel.add(undoButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Listener for selecting appointment
        appointmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedAppointment = appointmentList.getSelectedValue();
                if (selectedAppointment != null) {
                    staffComboBox.setSelectedItem(selectedAppointment.getStaff());
                    patientComboBox.setSelectedItem(selectedAppointment.getPatient());
                    dateField.setText(selectedAppointment.getDate());
                    timeField.setText(selectedAppointment.getTime());
                    typeComboBox.setSelectedItem(selectedAppointment.getType());
                    treatmentArea.setText(EncryptionDecorator.decrypt(selectedAppointment.getTreatmentDetails()));
                    prescriptionArea.setText(EncryptionDecorator.decrypt(selectedAppointment.getPrescription()));
                }
            }
        });

        new AppointmentController(this, loggedInStaff);
    }

    public void updateAppointmentList(Staff staff) {
        List<Appointment> appointments = service.getAppointmentsByStaff(staff);
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

    public void addUndoListener(ActionListener listener) {
        undoButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}