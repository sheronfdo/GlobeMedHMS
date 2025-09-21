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
}