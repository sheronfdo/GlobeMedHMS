package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.AuditLogRepository;
import com.jamith.globemedhms.infrastructure.repository.PatientRepository;
import com.jamith.globemedhms.presentation.views.pharmacist.DispenseMedView;
import com.jamith.globemedhms.presentation.views.pharmacist.ViewPrescriptionsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class PharmacistController {
    private final ViewPrescriptionsView viewPrescriptionsView;
    private final DispenseMedView dispenseMedView;
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();
    private final PatientService patientService = new PatientServiceImpl();
    private final Staff loggedInStaff;

    public PharmacistController(ViewPrescriptionsView view, Staff loggedInStaff) {
        this.viewPrescriptionsView = view;
        this.dispenseMedView = null;
        this.loggedInStaff = loggedInStaff;
        view.addRefreshListener(new RefreshListener());
    }

    public PharmacistController(DispenseMedView view, Staff loggedInStaff) {
        this.dispenseMedView = view;
        this.viewPrescriptionsView = null;
        this.loggedInStaff = loggedInStaff;
        view.addDispenseListener(new DispenseListener());
    }

    class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewPrescriptionsView.updateAppointmentList();
            viewPrescriptionsView.showMessage("Prescriptions refreshed.");
        }
    }

    class DispenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Appointment selectedAppointment = dispenseMedView.getSelectedAppointment();
            if (selectedAppointment != null) {
                String prescription = selectedAppointment.getPrescription();
                if (prescription != null && !prescription.isEmpty()) {
                    Patient patient = selectedAppointment.getPatient();
                    String historyEntry = "Medication dispensed by " + loggedInStaff.getName() + " on " + LocalDateTime.now() + ": " + prescription;
                    patient.appendHistory(historyEntry);
                    patientService.saveOrUpdatePatient(patient);
                    dispenseMedView.showMessage("Medication dispensed successfully!");
                    dispenseMedView.updateAppointmentList();
                    auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Dispensed medication for appointment ID: " + selectedAppointment.getId(), LocalDateTime.now()));
                } else {
                    dispenseMedView.showMessage("No prescription available for this appointment.");
                }
            } else {
                dispenseMedView.showMessage("Please select an appointment.");
            }
        }
    }
}