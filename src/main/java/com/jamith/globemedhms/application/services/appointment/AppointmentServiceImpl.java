package com.jamith.globemedhms.application.services.appointment;

import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.AppointmentRepository;
import com.jamith.globemedhms.patterns.builder.PatientBuilder;
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
    public List<Appointment> getAppointmentsByStaff(Staff staffId) {
        if (staffId.getRole().equals("ADMIN")) {
            return appointmentRepository.getAllAppointments();
        } else {
            return appointmentRepository.getAppointmentsByStaff(staffId.getId());
        }
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
        appointment.setTreatmentDetails((SanitizationDecorator.sanitize(treatmentDetails)));
        appointment.setPrescription((SanitizationDecorator.sanitize(prescription)));
        saveOrUpdateAppointment(appointment);

        Patient patient = appointment.getPatient();
        String historyEntry = "Appointment on " + appointment.getDate() + " at " + appointment.getTime() + " with " + appointment.getStaff().getName() + ". Treatment: " + treatmentDetails + ". Prescription: " + prescription;
        String currentHistory = patient.getHistory() != null ? EncryptionDecorator.decrypt(patient.getHistory()) : "";
        Patient updatedPatient = new PatientBuilder()
                .setName(patient.getName())
                .setDateOfBirth(patient.getDateOfBirth())
                .setAddress(patient.getAddress())
                .setMedicalHistory(EncryptionDecorator.decrypt(patient.getMedicalHistory()))
                .setTreatmentPlan(EncryptionDecorator.decrypt(patient.getTreatmentPlan()))
                .setHistory(currentHistory + "\n" + historyEntry)
                .build();
        updatedPatient.setId(patient.getId());
        System.out.println("Updated Patient ID: " + updatedPatient);
        patientService.saveOrUpdatePatient(updatedPatient);
        logger.info("Appointment completed for patient: {}", patient.getName());
    }

    @Override
    public List<Object[]> getAppointmentStatusCount() {
        return appointmentRepository.getAppointmentStatusCount();
    }

    @Override
    public List<Object[]> getAppointmentTypeCount() {
        return appointmentRepository.getAppointmentTypeCount();
    }

    @Override
    public List<Object[]> getMonthlyAppointmentTrend() {
        return appointmentRepository.getMonthlyAppointmentTrend();
    }

    @Override
    public List<Object[]> getStaffAppointmentCount() {
        return appointmentRepository.getStaffAppointmentCount();
    }

    @Override
    public long getTotalAppointmentCount() {
        return appointmentRepository.getTotalAppointmentCount();
    }

    @Override
    public long getCompletedAppointmentCount() {
        return appointmentRepository.getCompletedAppointmentCount();
    }

    // Additional utility methods
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentRepository.getAppointmentsByPatient(patientId);
    }

    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.getUpcomingAppointments();
    }
}