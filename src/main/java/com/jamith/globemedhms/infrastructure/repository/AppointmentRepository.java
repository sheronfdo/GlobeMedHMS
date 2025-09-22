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
            Query<Appointment> query = session.createQuery("FROM Appointment WHERE status!='CANCELLED'", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointments", e);
            return List.of();
        }
    }

    public List<Appointment> getAppointmentsByStaff(int staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment WHERE staff.id = :staffId and status!='CANCELLED'", Appointment.class);
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

    public List<Object[]> getAppointmentStatusCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT status, COUNT(*) FROM Appointment GROUP BY status ORDER BY COUNT(*) DESC", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointment status count", e);
            return List.of();
        }
    }

    public List<Object[]> getAppointmentTypeCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT type, COUNT(*) FROM Appointment GROUP BY type ORDER BY COUNT(*) DESC", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointment type count", e);
            return List.of();
        }
    }

    public List<Object[]> getMonthlyAppointmentTrend() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT FUNCTION('DATE_FORMAT', a.date, '%Y-%m'), COUNT(*) " +
                            "FROM Appointment a WHERE a.date IS NOT NULL " +
                            "GROUP BY FUNCTION('DATE_FORMAT', a.date, '%Y-%m') " +
                            "ORDER BY FUNCTION('DATE_FORMAT', a.date, '%Y-%m') DESC", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching monthly appointment trend", e);
            return List.of();
        }
    }

    public List<Object[]> getStaffAppointmentCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT s.name, COUNT(a) FROM Appointment a JOIN a.staff s " +
                            "GROUP BY s.name ORDER BY COUNT(a) DESC", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching staff appointment count", e);
            return List.of();
        }
    }

    public long getTotalAppointmentCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Appointment", Long.class);
            Long result = query.uniqueResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            logger.error("Error fetching total appointment count", e);
            return 0L;
        }
    }

    public long getCompletedAppointmentCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Appointment WHERE status = 'COMPLETED'", Long.class);
            Long result = query.uniqueResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            logger.error("Error fetching completed appointment count", e);
            return 0L;
        }
    }

    // Additional utility methods for enhanced functionality

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment WHERE patient.id = :patientId ORDER BY date DESC, time DESC", Appointment.class);
            query.setParameter("patientId", patientId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointments for patient ID: {}", patientId, e);
            return List.of();
        }
    }

    public List<Appointment> getAppointmentsByDate(String date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment WHERE date = :date AND status != 'CANCELLED' ORDER BY time ASC", Appointment.class);
            query.setParameter("date", date);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointments for date: {}", date, e);
            return List.of();
        }
    }

    public List<Appointment> getUpcomingAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment WHERE status = 'SCHEDULED' ORDER BY date ASC, time ASC", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching upcoming appointments", e);
            return List.of();
        }
    }

    public List<Appointment> getAppointmentsByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment WHERE status = :status ORDER BY date DESC, time DESC", Appointment.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointments with status: {}", status, e);
            return List.of();
        }
    }

    public double getAverageAppointmentDuration() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // This is a placeholder - you might need to store appointment duration in your entity
            Query<Double> query = session.createQuery(
                    "SELECT AVG(30) FROM Appointment WHERE status = 'COMPLETED'", Double.class);
            Double result = query.uniqueResult();
            return result != null ? result : 30.0; // Default 30 minutes
        } catch (Exception e) {
            logger.error("Error calculating average appointment duration", e);
            return 30.0;
        }
    }

    public List<Object[]> getDailyAppointmentCount(int days) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT date, COUNT(*) FROM Appointment " +
                            "WHERE date >= FUNCTION('DATE_SUB', CURRENT_DATE, :days, 'DAY') " +
                            "GROUP BY date ORDER BY date DESC", Object[].class);
            query.setParameter("days", days);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching daily appointment count for last {} days", days, e);
            return List.of();
        }
    }

    public Appointment findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Appointment.class, id);
        } catch (Exception e) {
            logger.error("Error finding appointment by ID: {}", id, e);
            return null;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null) {
                session.remove(appointment);
                session.getTransaction().commit();
                logger.info("Deleted appointment ID: {}", appointmentId);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting appointment ID: {}", appointmentId, e);
            return false;
        }
    }

    public List<Object[]> getAppointmentRevenueAnalysis() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT a.type, COUNT(a), " +
                            "(SELECT COUNT(b) FROM Billing b WHERE b.appointment = a AND b.status = 'PAID') " +
                            "FROM Appointment a GROUP BY a.type", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching appointment revenue analysis", e);
            return List.of();
        }
    }
}