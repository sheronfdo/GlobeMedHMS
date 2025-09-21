package com.jamith.globemedhms.application.services.staff;

import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.StaffRepository;
import com.jamith.globemedhms.patterns.flyweight.RoleFlyweight;
import com.jamith.globemedhms.patterns.flyweight.RoleFlyweightFactory;
import com.jamith.globemedhms.patterns.roleobject.AdminRole;
import com.jamith.globemedhms.patterns.roleobject.DoctorRole;
import com.jamith.globemedhms.patterns.roleobject.NurseRole;
import com.jamith.globemedhms.patterns.roleobject.PharmacistRole;
import com.jamith.globemedhms.patterns.roleobject.Role;
import com.jamith.globemedhms.util.PasswordHasher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class StaffServiceImpl implements StaffService {
    private static final Logger logger = LogManager.getLogger(StaffServiceImpl.class);
    private final StaffRepository staffRepository = new StaffRepository();

    public StaffServiceImpl() {
        createDefaultAdminIfNotExists();
    }

    private void createDefaultAdminIfNotExists() {
        if (staffRepository.findByUsername("admin") == null) {
            String defaultPasswordHash = PasswordHasher.hashPassword("admin");
            Staff defaultAdmin = new Staff("Default Admin", "admin", defaultPasswordHash, "ADMIN");
            assignRole(defaultAdmin, "ADMIN");
            saveOrUpdateStaff(defaultAdmin);
            logger.info("Default admin user created.");
        } else {
            logger.info("Default admin user already exists.");
        }
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    @Override
    public void saveOrUpdateStaff(Staff staff) {
        staffRepository.saveOrUpdate(staff);
    }

    @Override
    public boolean authenticate(String username, String password) {
        Staff staff = staffRepository.findByUsername(username);
        if (staff != null && BCrypt.checkpw(password, staff.getPasswordHash())) {
            logger.info("Authentication successful for {}", username);
            return true;
        }
        logger.warn("Authentication failed for {}", username);
        return false;
    }

    @Override
    public void assignRole(Staff staff, String roleName) {
        RoleFlyweight flyweight = RoleFlyweightFactory.getRoleFlyweight(roleName);
        staff.setRoleFlyweight(flyweight);
        staff.setRole(roleName.toUpperCase());
        saveOrUpdateStaff(staff);
        RoleFlyweightFactory.getRoleFlyweight(roleName);
        logger.info("Assigned role {} to staff {}", roleName, staff.getUsername());
    }

    @Override
    public void revokeRole(Staff staff, String roleName) {
        staff.setRoleFlyweight(null);
        staff.setRole("UNKNOWN"); // staff.getRoles().isEmpty() ? "UNKNOWN" : staff.getRoles().get(0).getClass().getSimpleName().replace("Role", "").toUpperCase()
        saveOrUpdateStaff(staff);
        logger.info("Revoked role {} from staff {}", roleName, staff.getUsername());
    }

    private Role createRole(String roleName) {
        return switch (roleName.toUpperCase()) {
            case "DOCTOR" -> new DoctorRole();
            case "NURSE" -> new NurseRole();
            case "PHARMACIST" -> new PharmacistRole();
            case "ADMIN" -> new AdminRole();
            default -> throw new IllegalArgumentException("Unknown role: " + roleName);
        };
    }

    @Override
    public Staff getLoggedInStaff(String username) {
        return staffRepository.findByUsername(username);
    }
}