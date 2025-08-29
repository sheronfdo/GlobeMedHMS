package com.jamith.globemedhms.presentation.views.nurse;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.presentation.controllers.AdministerMedController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class AdministerMedView extends JPanel {
    private final PatientService patientService = new PatientServiceImpl();
    private JList<Patient> patientList;
    private JTextArea medNotesArea;
    private JTextField medicationField;
    private JTextField dosageField;
    private JButton administerButton;

    public AdministerMedView(Staff loggedInStaff) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Patient list with scroll pane
        patientList = new JList<>();
        updatePatientList();
        JScrollPane patientListScroll = new JScrollPane(patientList);
        patientListScroll.setPreferredSize(new Dimension(300, 0));
        add(patientListScroll, BorderLayout.WEST);

        // Right panel for administration details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Administer Medications"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Medication:"), gbc);
        gbc.gridx = 1;
        medicationField = new JTextField(20);
        detailsPanel.add(medicationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Dosage (e.g., 400mg):"), gbc);
        gbc.gridx = 1;
        dosageField = new JTextField(20);
        detailsPanel.add(dosageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Administration Notes:"), gbc);
        gbc.gridx = 1;
        medNotesArea = new JTextArea(5, 20);
        detailsPanel.add(new JScrollPane(medNotesArea), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        administerButton = new JButton("Administer and Record");
        detailsPanel.add(administerButton, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        // Listener for selecting patient
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Patient selectedPatient = patientList.getSelectedValue();
                if (selectedPatient != null) {
                    medNotesArea.setText(selectedPatient.getHistory());
                }
            }
        });

        new AdministerMedController(this, loggedInStaff);
    }

    public void updatePatientList() {
        List<Patient> patients = patientService.getAllPatients();
        patientList.setListData(patients.toArray(new Patient[0]));
    }

    public Patient getSelectedPatient() {
        return patientList.getSelectedValue();
    }

    public String getMedication() {
        return medicationField.getText();
    }

    public String getDosage() {
        return dosageField.getText();
    }

    public String getMedNotes() {
        return medNotesArea.getText();
    }

    public void addAdministerListener(ActionListener listener) {
        administerButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}