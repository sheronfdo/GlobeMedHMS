package com.jamith.globemedhms.infrastructure.repository;

import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class AuditLogRepository {
    private static final Logger logger = LogManager.getLogger(AuditLogRepository.class);

    public void save(AuditLog auditLog) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(auditLog);
            session.getTransaction().commit();
            logger.info("Saved audit log for staff ID: {}", auditLog.getStaffId());
        } catch (Exception e) {
            logger.error("Error saving audit log", e);
        }
    }
}