package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.staff.StaffService;
import com.jamith.globemedhms.application.services.staff.StaffServiceImpl;
import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.AuditLogRepository;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.staff.StaffView;
import com.jamith.globemedhms.util.PasswordHasher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class StaffController {
    private final StaffView view;
    private final StaffService service = new StaffServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy();
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();

    public StaffController(StaffView view) {
        this.view = view;
        view.addAddStaffListener(new AddStaffListener());
        view.addAssignRoleListener(new AssignRoleListener());
        view.addRevokeRoleListener(new RevokeRoleListener());
        view.addAccessResourceListener(new AccessResourceListener());
    }

    class AddStaffListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = view.getNewStaffName();
            String username = view.getNewStaffUsername();
            String password = view.getNewStaffPassword();
            String role = view.getSelectedRole();
            if (name != null && !name.isEmpty() && username != null && !username.isEmpty() && password != null && !password.isEmpty() && role != null) {
                String passwordHash = PasswordHasher.hashPassword(password);
                Staff newStaff = new Staff(name, username, passwordHash, role.toUpperCase());
                service.assignRole(newStaff, role);
                service.saveOrUpdateStaff(newStaff);
                auditLogRepository.save(new AuditLog(newStaff.getId(), "Staff added: " + username, LocalDateTime.now()));
                view.updateStaffList();
                view.showMessage("Staff added successfully!");
            } else {
                view.showMessage("Please fill all fields.");
            }
        }
    }

    class AssignRoleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Staff selectedStaff = view.getSelectedStaff();
            String role = view.getSelectedRole();
            if (selectedStaff != null && role != null) {
                service.assignRole(selectedStaff, role);
                auditLogRepository.save(new AuditLog(selectedStaff.getId(), "Assigned role: " + role, LocalDateTime.now()));
                view.updateStaffList();
                view.showMessage("Role assigned successfully!");
            } else {
                view.showMessage("Please select a staff and role.");
            }
        }
    }

    class RevokeRoleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Staff selectedStaff = view.getSelectedStaff();
            String role = view.getSelectedRole();
            if (selectedStaff != null && role != null) {
                service.revokeRole(selectedStaff, role);
                auditLogRepository.save(new AuditLog(selectedStaff.getId(), "Revoked role: " + role, LocalDateTime.now()));
                view.updateStaffList();
                view.showMessage("Role revoked successfully!");
            } else {
                view.showMessage("Please select a staff and role.");
            }
        }
    }

    class AccessResourceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Staff selectedStaff = view.getSelectedStaff();
            String resource = "PATIENT_RECORDS";
            String permission = "VIEW_PATIENT_RECORDS";
            if (selectedStaff != null) {
                try {
                    proxy.accessResource(selectedStaff, resource, permission);
                    view.showMessage("Access granted!");
                } catch (SecurityException ex) {
                    view.showMessage(ex.getMessage());
                }
            } else {
                view.showMessage("Please select a staff member.");
            }
        }
    }
}