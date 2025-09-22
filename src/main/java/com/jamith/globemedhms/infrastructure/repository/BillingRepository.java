package com.jamith.globemedhms.infrastructure.repository;

import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BillingRepository {
    private static final Logger logger = LogManager.getLogger(BillingRepository.class);

    public List<Billing> getAllBillings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery("FROM Billing", Billing.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching billings", e);
            return List.of();
        }
    }

    public Billing saveOrUpdate(Billing billing) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            billing = session.merge(billing);
            session.getTransaction().commit();
            logger.info("Saved/Updated billing ID: {}", billing.getId());
        } catch (Exception e) {
            logger.error("Error saving/updating billing", e);
        }
        return billing;
    }

    public Billing findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Billing.class, id);
        } catch (Exception e) {
            logger.error("Error finding billing by ID: {}", id, e);
            return null;
        }
    }

    public Billing findByAppointmentId(int appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery("FROM Billing WHERE appointment.id = :appointmentId", Billing.class);
            query.setParameter("appointmentId", appointmentId);
            Billing billing = query.uniqueResult();
            if (billing == null) {
                logger.warn("No billing found for appointment ID: {}", appointmentId);
            }
            return billing;
        } catch (Exception e) {
            logger.error("Error finding billing by appointment ID: {}", appointmentId, e);
            return null;
        }
    }

    public List<Object[]> getMonthlyRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m'), SUM(amount) " +
                            "FROM Billing WHERE status = 'PAID' GROUP BY FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m')",
                    Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching monthly revenue", e);
            return List.of();
        }
    }

    public List<Object[]> getBillingStatusDistribution() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT status, COUNT(*) FROM Billing GROUP BY status", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching billing status distribution", e);
            return List.of();
        }
    }

    public List<Object[]> getBillingTypeDistribution() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT billingType, COUNT(*) FROM Billing GROUP BY billingType", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching billing type distribution", e);
            return List.of();
        }
    }

    public double getTotalRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                    "SELECT SUM(amount) FROM Billing WHERE status = 'PAID'", Double.class);
            Double result = query.uniqueResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            logger.error("Error fetching total revenue", e);
            return 0.0;
        }
    }

    public long getTotalBillingCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Billing", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error fetching total billing count", e);
            return 0;
        }
    }

    public long getInsuranceBillingCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Billing WHERE billingType = 'INSURANCE'", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error fetching insurance billing count", e);
            return 0;
        }
    }

    public List<Billing> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing WHERE status = :status ORDER BY id DESC", Billing.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching billings with status: {}", status, e);
            return List.of();
        }
    }

    public List<Billing> getPendingBillings() {
        return findByStatus("PENDING");
    }

    public List<Billing> getPendingInsuranceBillings() {
        return findByStatus("INSURANCE_PENDING");
    }
}