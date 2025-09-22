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

    public List<Object[]> getPatientAgeDistribution() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT CASE " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) < 18 THEN 'Under 18' " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) BETWEEN 18 AND 30 THEN '18-30' " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) BETWEEN 31 AND 45 THEN '31-45' " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) BETWEEN 46 AND 60 THEN '46-60' " +
                            "ELSE 'Over 60' END, COUNT(*) " +
                            "FROM Patient GROUP BY CASE " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) < 18 THEN 'Under 18' " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) BETWEEN 18 AND 30 THEN '18-30' " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) BETWEEN 31 AND 45 THEN '31-45' " +
                            "WHEN TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()) BETWEEN 46 AND 60 THEN '46-60' " +
                            "ELSE 'Over 60' END",
                    Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching patient age distribution", e);
            return List.of();
        }
    }

    public List<Object[]> getMonthlyRegistrationTrend() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Using current timestamp as registration date (you might need to add registrationDate field)
            Query<Object[]> query = session.createQuery(
                    "SELECT FUNCTION('DATE_FORMAT', CURRENT_TIMESTAMP, '%Y-%m'), COUNT(*) " +
                            "FROM Patient GROUP BY FUNCTION('DATE_FORMAT', CURRENT_TIMESTAMP, '%Y-%m')",
                    Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching monthly registration trend", e);
            return List.of();
        }
    }

    public long getTotalPatientCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Patient", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error fetching total patient count", e);
            return 0;
        }
    }
}