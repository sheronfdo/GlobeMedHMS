package com.jamith.globemedhms.presentation.views.patient;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.presentation.controllers.PatientController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class PatientView extends JPanel {
    private final PatientService service = new PatientServiceImpl();
    private JList<Patient> patientList;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField addressField;
    private JTextArea medicalHistoryArea;
    private JTextArea treatmentPlanArea;
    private JButton addPatientButton;
    private JButton updatePatientButton;

    public PatientView(Staff loggedInStaff) {
//        setLayout(new BorderLayout(10, 10));
//        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        patientList = new JList<>();
//        updatePatientList();
//        JScrollPane patientListScroll = new JScrollPane(patientList);
//        add(patientListScroll, BorderLayout.WEST);
//
//        JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
//        detailsPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
//        detailsPanel.add(new JLabel("Name:"));
//        nameField = new JTextField(20);
//        detailsPanel.add(nameField);
//        detailsPanel.add(new JLabel("Date of Birth:"));
//        dobField = new JTextField(20);
//        detailsPanel.add(dobField);
//        detailsPanel.add(new JLabel("Address:"));
//        addressField = new JTextField(20);
//        detailsPanel.add(addressField);
//        detailsPanel.add(new JLabel("Medical History:"));
//        medicalHistoryArea = new JTextArea(5, 20);
//        detailsPanel.add(new JScrollPane(medicalHistoryArea));
//        detailsPanel.add(new JLabel("Treatment Plan:"));
//        treatmentPlanArea = new JTextArea(5, 20);
//        detailsPanel.add(new JScrollPane(treatmentPlanArea));
//        addPatientButton = new JButton("Add Patient");
//        detailsPanel.add(addPatientButton);
//        updatePatientButton = new JButton("Update Patient");
//        detailsPanel.add(updatePatientButton);
//        add(detailsPanel, BorderLayout.CENTER);
//
//        patientList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                Patient selectedPatient = patientList.getSelectedValue();
//                if (selectedPatient != null) {
//                    nameField.setText(selectedPatient.getName());
//                    dobField.setText(selectedPatient.getDateOfBirth());
//                    addressField.setText(selectedPatient.getAddress());
//                    medicalHistoryArea.setText(EncryptionDecorator.decrypt(selectedPatient.getMedicalHistory()));
//                    treatmentPlanArea.setText(EncryptionDecorator.decrypt(selectedPatient.getTreatmentPlan()));
//                }
//            }
//        });
//        new PatientController(this, loggedInStaff);



        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Left: Patient List
        patientList = new JList<>();
        updatePatientList();
        patientList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        patientList.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        patientList.setSelectionBackground(new Color(100, 149, 237));
        JScrollPane patientListScroll = new JScrollPane(patientList);
        patientListScroll.setBorder(BorderFactory.createTitledBorder("Patients"));
        patientListScroll.setPreferredSize(new Dimension(220, 0));
        add(patientListScroll, BorderLayout.WEST);

        // Center: Patient Details Form
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx++;
        nameField = new JTextField(20);
        nameField.setBorder(new LineBorder(Color.GRAY, 1));
        detailsPanel.add(nameField, gbc);

        // Date of Birth
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx++;
        dobField = new JTextField(20);
        dobField.setBorder(new LineBorder(Color.GRAY, 1));
        detailsPanel.add(dobField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx++;
        addressField = new JTextField(20);
        addressField.setBorder(new LineBorder(Color.GRAY, 1));
        detailsPanel.add(addressField, gbc);

        // Medical History
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Medical History:"), gbc);
        gbc.gridx++;
        medicalHistoryArea = new JTextArea(5, 20);
        medicalHistoryArea.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        detailsPanel.add(new JScrollPane(medicalHistoryArea), gbc);

        // Treatment Plan
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Treatment Plan:"), gbc);
        gbc.gridx++;
        treatmentPlanArea = new JTextArea(5, 20);
        treatmentPlanArea.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        detailsPanel.add(new JScrollPane(treatmentPlanArea), gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        addPatientButton = new JButton("Add Patient");
        addPatientButton.setBackground(new Color(60, 179, 113));
        addPatientButton.setForeground(Color.WHITE);
        addPatientButton.setFocusPainted(false);
        buttonPanel.add(addPatientButton);

        updatePatientButton = new JButton("Update Patient");
        updatePatientButton.setBackground(new Color(65, 105, 225));
        updatePatientButton.setForeground(Color.WHITE);
        updatePatientButton.setFocusPainted(false);
        buttonPanel.add(updatePatientButton);

        detailsPanel.add(buttonPanel, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        // Patient selection listener
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Patient selectedPatient = patientList.getSelectedValue();
                if (selectedPatient != null) {
                    nameField.setText(selectedPatient.getName());
                    dobField.setText(selectedPatient.getDateOfBirth());
                    addressField.setText(selectedPatient.getAddress());
                    medicalHistoryArea.setText(EncryptionDecorator.decrypt(selectedPatient.getMedicalHistory()));
                    treatmentPlanArea.setText(EncryptionDecorator.decrypt(selectedPatient.getTreatmentPlan()));
                }
            }
        });

        new PatientController(this, loggedInStaff);

    }

    public void updatePatientList() {
        List<Patient> patients = service.getAllPatients();
        patientList.setListData(patients.toArray(new Patient[0]));
    }

    public Patient getSelectedPatient() {
        return patientList.getSelectedValue();
    }

    public String getName() {
        return nameField.getText();
    }

    public String getDateOfBirth() {
        return dobField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public String getMedicalHistory() {
        return medicalHistoryArea.getText();
    }

    public String getTreatmentPlan() {
        return treatmentPlanArea.getText();
    }

    public void addAddPatientListener(ActionListener listener) {
        addPatientButton.addActionListener(listener);
    }

    public void addUpdatePatientListener(ActionListener listener) {
        updatePatientButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}