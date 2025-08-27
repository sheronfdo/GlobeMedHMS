package com.jamith.globemedhms.infrastructure.repository;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AppointmentRepository {
    private static final Logger logger = LogManager.getLogger(AppointmentRepository.class);

    public List<Appointment> getAllAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointments", e);
            return List.of();
        }
    }

    public List<Appointment> getAppointmentsByStaff(int staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment WHERE staff.id = :staffId", Appointment.class);
            query.setParameter("staffId", staffId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointments for staff ID: {}", staffId, e);
            return List.of();
        }
    }

    public void saveOrUpdate(Appointment appointment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(appointment);
            session.getTransaction().commit();
            logger.info("Saved/Updated appointment for patient: {}", appointment.getPatient().getName());
        } catch (Exception e) {
            logger.error("Error saving/updating appointment", e);
        }
    }

    public boolean hasConflict(int staffId, String date, String time) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Appointment WHERE staff.id = :staffId AND date = :date AND time = :time", Long.class);
            query.setParameter("staffId", staffId);
            query.setParameter("date", date);
            query.setParameter("time", time);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Error checking appointment conflict", e);
            return false;
        }
    }
}