package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.patient.PatientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PatientController {
    private final PatientView view;
    private final PatientService service = new PatientServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy(); // For permission checks

    public PatientController(PatientView view, Staff loggedInStaff) {
        this.view = view;
        view.addAddPatientListener(new AddPatientListener(loggedInStaff));
        view.addUpdatePatientListener(new UpdatePatientListener(loggedInStaff));
    }

    class AddPatientListener implements ActionListener {
        private final Staff staff;

        public AddPatientListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "PATIENT_RECORDS", "UPDATE_PATIENT_RECORDS");
                String name = view.getName();
                String dob = view.getDateOfBirth();
                String address = view.getAddress();
                String medicalHistory = EncryptionDecorator.encrypt(view.getMedicalHistory()); // Decorator for security
                String treatmentPlan = EncryptionDecorator.encrypt(view.getTreatmentPlan());
                if (name != null && !name.isEmpty() && dob != null && !dob.isEmpty()) {
                    Patient newPatient = new Patient(name, dob, address, medicalHistory, treatmentPlan);
                    service.saveOrUpdatePatient(newPatient);
                    view.updatePatientList();
                    view.showMessage("Patient added successfully!");
                } else {
                    view.showMessage("Please fill required fields.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }

    class UpdatePatientListener implements ActionListener {
        private final Staff staff;

        public UpdatePatientListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "PATIENT_RECORDS", "UPDATE_PATIENT_RECORDS");
                Patient selectedPatient = view.getSelectedPatient();
                if (selectedPatient != null) {
                    selectedPatient.setName(view.getName());
                    selectedPatient.setDateOfBirth(view.getDateOfBirth());
                    selectedPatient.setAddress(view.getAddress());
                    selectedPatient.setMedicalHistory(EncryptionDecorator.encrypt(view.getMedicalHistory()));
                    selectedPatient.setTreatmentPlan(EncryptionDecorator.encrypt(view.getTreatmentPlan()));
                    service.saveOrUpdatePatient(selectedPatient);
                    view.updatePatientList();
                    view.showMessage("Patient updated successfully!");
                } else {
                    view.showMessage("Please select a patient.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }
}