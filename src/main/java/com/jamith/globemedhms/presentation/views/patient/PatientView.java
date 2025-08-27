package com.jamith.globemedhms.presentation.views.patient;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.presentation.controllers.PatientController;

import javax.swing.*;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Patient list with scroll pane
        patientList = new JList<>();
        updatePatientList();
        JScrollPane patientListScroll = new JScrollPane(patientList);
        add(patientListScroll, BorderLayout.WEST);

        // Right panel for patient details (GridLayout)
        JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
        detailsPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        detailsPanel.add(nameField);
        detailsPanel.add(new JLabel("Date of Birth:"));
        dobField = new JTextField(20);
        detailsPanel.add(dobField);
        detailsPanel.add(new JLabel("Address:"));
        addressField = new JTextField(20);
        detailsPanel.add(addressField);
        detailsPanel.add(new JLabel("Medical History:"));
        medicalHistoryArea = new JTextArea(5, 20);
        detailsPanel.add(new JScrollPane(medicalHistoryArea));
        detailsPanel.add(new JLabel("Treatment Plan:"));
        treatmentPlanArea = new JTextArea(5, 20);
        detailsPanel.add(new JScrollPane(treatmentPlanArea));
        addPatientButton = new JButton("Add Patient");
        detailsPanel.add(addPatientButton);
        updatePatientButton = new JButton("Update Patient");
        detailsPanel.add(updatePatientButton);
        add(detailsPanel, BorderLayout.CENTER);

        // Listener for selecting patient from list
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