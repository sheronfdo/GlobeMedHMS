package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.builder.PatientBuilder;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.nurse.AdministerMedView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class AdministerMedController {
    private final AdministerMedView view;
    private final PatientService patientService = new PatientServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy();

    public AdministerMedController(AdministerMedView view, Staff loggedInStaff) {
        this.view = view;
        view.addAdministerListener(new AdministerListener(loggedInStaff));
    }

    class AdministerListener implements ActionListener {
        private final Staff staff;

        public AdministerListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "ADMINISTER_MEDICATIONS", "ADMINISTER_MEDICATIONS");
                Patient selectedPatient = view.getSelectedPatient();
                if (selectedPatient == null) {
                    view.showMessage("Please select a patient.");
                    return;
                }
                String medication = view.getMedication();
                String dosage = view.getDosage();
                String notes = view.getMedNotes();
                if (medication == null || medication.trim().isEmpty() || dosage == null || dosage.trim().isEmpty()) {
                    view.showMessage("Medication and dosage fields cannot be empty.");
                    return;
                }
                String historyEntry = String.format("Medication administered by %s on %s: %s, Dosage: %s, Notes: %s",
                        staff.getName(), LocalDateTime.now(), medication, dosage, notes);
                String currentHistory = selectedPatient.getHistory() == null ? "" : EncryptionDecorator.decrypt(selectedPatient.getHistory());

                Patient updatedPatient = new PatientBuilder()
                        .setName(selectedPatient.getName())
                        .setDateOfBirth(selectedPatient.getDateOfBirth())
                        .setAddress(selectedPatient.getAddress())
                        .setMedicalHistory(EncryptionDecorator.decrypt(selectedPatient.getMedicalHistory()))
                        .setTreatmentPlan(EncryptionDecorator.decrypt(selectedPatient.getTreatmentPlan()))
                        .setHistory(currentHistory + "\n" + historyEntry)
                        .build();

                updatedPatient.setId(selectedPatient.getId());
                patientService.saveOrUpdatePatient(updatedPatient);

                view.updatePatientList();
                view.showMessage("Medication administered and recorded successfully!");
            } catch (SecurityException ex) {
                view.showMessage("Access denied: " + ex.getMessage());
            }
        }
    }
}