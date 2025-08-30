package com.jamith.globemedhms.application.services.appointment;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.infrastructure.repository.AppointmentRepository;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import com.jamith.globemedhms.patterns.decorator.SanitizationDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {
    private static final Logger logger = LogManager.getLogger(AppointmentServiceImpl.class);
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private final PatientService patientService = new PatientServiceImpl();

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.getAllAppointments();
    }

    @Override
    public List<Appointment> getAppointmentsByStaff(int staffId) {
        return appointmentRepository.getAppointmentsByStaff(staffId);
    }

    @Override
    public void saveOrUpdateAppointment(Appointment appointment) {
        if (appointment.getId() == 0) {
            if (hasConflict(appointment.getStaff().getId(), appointment.getDate(), appointment.getTime())) {
                throw new IllegalStateException("Conflict: Staff is already booked at this time.");
            }
        }
        if (appointment.getTreatmentDetails() != null) {
            appointment.setTreatmentDetails(EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(appointment.getTreatmentDetails())));
        }
        if (appointment.getPrescription() != null) {
            appointment.setPrescription(EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(appointment.getPrescription())));
        }
        appointmentRepository.saveOrUpdate(appointment);
    }

    @Override
    public boolean hasConflict(int staffId, String date, String time) {
        return appointmentRepository.hasConflict(staffId, date, time);
    }

    @Override
    public void completeAppointment(Appointment appointment, String treatmentDetails, String prescription) {
        appointment.setStatus("COMPLETED");
        appointment.setTreatmentDetails(EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(treatmentDetails)));
        appointment.setPrescription(EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(prescription)));
        saveOrUpdateAppointment(appointment);

        Patient patient = appointment.getPatient();
        String historyEntry = "Appointment on " + appointment.getDate() + " at " + appointment.getTime() + " with " + appointment.getStaff().getName() + ". Treatment: " + treatmentDetails + ". Prescription: " + prescription;
        patient.setMedicalHistory(EncryptionDecorator.encrypt(SanitizationDecorator.sanitize(patient.getMedicalHistory() + "\n" + historyEntry)));
        patientService.saveOrUpdatePatient(patient);
        logger.info("Appointment completed for patient: {}", patient.getName());
    }
}