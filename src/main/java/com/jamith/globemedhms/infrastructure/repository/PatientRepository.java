package com.jamith.globemedhms.infrastructure.repository;

import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PatientRepository {
    private static final Logger logger = LogManager.getLogger(PatientRepository.class);

    public List<Patient> getAllPatients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery("FROM Patient", Patient.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching patients", e);
            return List.of();
        }
    }

    public void saveOrUpdate(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(patient);
            session.getTransaction().commit();
            logger.info("Saved/Updated patient: {}", patient.getName());
        } catch (Exception e) {
            logger.error("Error saving/updating patient", e);
        }
    }

    public Patient findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Patient.class, id);
        } catch (Exception e) {
            logger.error("Error finding patient by ID: {}", id, e);
            return null;
        }
    }
}