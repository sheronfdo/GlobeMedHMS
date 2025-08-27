package com.jamith.globemedhms.application.services.patient;

import com.jamith.globemedhms.core.entities.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
    void saveOrUpdatePatient(Patient patient);
    Patient getPatientById(int id);
}