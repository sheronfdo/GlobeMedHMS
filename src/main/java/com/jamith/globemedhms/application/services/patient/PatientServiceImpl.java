package com.jamith.globemedhms.application.services.patient;

import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.infrastructure.repository.PatientRepository;
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
        patientRepository.saveOrUpdate(patient);
    }

    @Override
    public Patient getPatientById(int id) {
        return patientRepository.findById(id);
    }
}