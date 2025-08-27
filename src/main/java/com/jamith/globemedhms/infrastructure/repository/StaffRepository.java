package com.jamith.globemedhms.infrastructure.repository;

import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;
import com.jamith.globemedhms.patterns.roleobject.AdminRole;
import com.jamith.globemedhms.patterns.roleobject.DoctorRole;
import com.jamith.globemedhms.patterns.roleobject.NurseRole;
import com.jamith.globemedhms.patterns.roleobject.PharmacistRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StaffRepository {
    private static final Logger logger = LogManager.getLogger(StaffRepository.class);

    public List<Staff> getAllStaff() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery("FROM Staff", Staff.class);
            List<Staff> staffList = query.getResultList();
            for (Staff staff : staffList) {
                switch (staff.getRole()) {
                    case "DOCTOR" -> staff.addRole(new DoctorRole());
                    case "NURSE" -> staff.addRole(new NurseRole());
                    case "PHARMACIST" -> staff.addRole(new PharmacistRole());
                    case "ADMIN" -> staff.addRole(new AdminRole());
                }
            }
            return staffList;
        } catch (Exception e) {
            logger.error("Error fetching staff", e);
            return List.of();
        }
    }

    public void saveOrUpdate(Staff staff) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(staff);
            session.getTransaction().commit();
            logger.info("Saved/Updated staff: {}", staff.getUsername());
        } catch (Exception e) {
            logger.error("Error saving/updating staff", e);
        }
    }

    public Staff findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery("FROM Staff WHERE username = :username", Staff.class);
            query.setParameter("username", username);
            Staff staff = query.uniqueResult();
            if (staff != null) {
                switch (staff.getRole()) {
                    case "DOCTOR" -> staff.addRole(new DoctorRole());
                    case "NURSE" -> staff.addRole(new NurseRole());
                    case "PHARMACIST" -> staff.addRole(new PharmacistRole());
                    case "ADMIN" -> staff.addRole(new AdminRole());
                }
            }
            return staff;
        } catch (Exception e) {
            logger.error("Error finding staff by username: {}", username, e);
            return null;
        }
    }
}