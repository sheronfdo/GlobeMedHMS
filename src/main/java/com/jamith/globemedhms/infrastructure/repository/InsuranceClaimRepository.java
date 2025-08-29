package com.jamith.globemedhms.infrastructure.repository;

import com.jamith.globemedhms.core.entities.InsuranceClaim;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;
    import org.hibernate.Session;
    import org.hibernate.query.Query;

    import java.util.List;

    public class InsuranceClaimRepository {
        private static final Logger logger = LogManager.getLogger(InsuranceClaimRepository.class);

        public List<InsuranceClaim> getAllClaims() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<InsuranceClaim> query = session.createQuery("FROM InsuranceClaim", InsuranceClaim.class);
                return query.getResultList();
            } catch (Exception e) {
                logger.error("Error fetching claims", e);
                return List.of();
            }
        }

        public void saveOrUpdate(InsuranceClaim claim) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.merge(claim);
                session.getTransaction().commit();
                logger.info("Saved/Updated claim ID: {}", claim.getId());
            } catch (Exception e) {
                logger.error("Error saving/updating claim", e);
            }
        }

        public InsuranceClaim findById(int id) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.get(InsuranceClaim.class, id);
            } catch (Exception e) {
                logger.error("Error finding claim by ID: {}", id, e);
                return null;
            }
        }
    }