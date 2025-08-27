package com.jamith.globemedhms.application.services.staff;

import com.jamith.globemedhms.core.entities.Staff;

import java.util.List;

public interface StaffService {
    List<Staff> getAllStaff();
    void saveOrUpdateStaff(Staff staff);
    boolean authenticate(String username, String password);
    void assignRole(Staff staff, String roleName);
    void revokeRole(Staff staff, String roleName);
    Staff getLoggedInStaff(String username);
}