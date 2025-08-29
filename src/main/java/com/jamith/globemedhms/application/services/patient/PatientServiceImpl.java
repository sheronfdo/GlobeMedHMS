package com.jamith.globemedhms.application.services.patient;

import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.infrastructure.repository.PatientRepository;
import com.jamith.globemedhms.patterns.decorator.EncryptionDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PatientServiceImpl implements PatientService {
    private static final Logger logger = LogManager.getLogger(PatientServiceImpl.class);
    private final PatientRepository patientRepository = new PatientRepository();

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.getAllPatients();
    }

    @Override
    public void saveOrUpdatePatient(Patient patient) {
        try {
            // Encrypt sensitive fields before saving
            if (patient.getMedicalHistory() != null) {
                patient.setMedicalHistory(EncryptionDecorator.encrypt(patient.getMedicalHistory()));
            }
            if (patient.getTreatmentPlan() != null) {
                patient.setTreatmentPlan(EncryptionDecorator.encrypt(patient.getTreatmentPlan()));
            }
            if (patient.getHistory() != null) {
                patient.setHistory(EncryptionDecorator.encrypt(patient.getHistory()));
            }
            patientRepository.saveOrUpdate(patient);
            logger.info("Saved/Updated patient: {}", patient.getName());
        } catch (Exception e) {
            logger.error("Error saving/updating patient: {}", patient.getName(), e);
        }
    }

    @Override
    public Patient getPatientById(int id) {
        Patient patient = patientRepository.findById(id);
        if (patient != null) {
            // Decrypt sensitive fields for display
            if (patient.getMedicalHistory() != null) {
                patient.setMedicalHistory(EncryptionDecorator.decrypt(patient.getMedicalHistory()));
            }
            if (patient.getTreatmentPlan() != null) {
                patient.setTreatmentPlan(EncryptionDecorator.decrypt(patient.getTreatmentPlan()));
            }
            if (patient.getHistory() != null) {
                patient.setHistory(EncryptionDecorator.decrypt(patient.getHistory()));
            }
        }
        return patient;
    }
}