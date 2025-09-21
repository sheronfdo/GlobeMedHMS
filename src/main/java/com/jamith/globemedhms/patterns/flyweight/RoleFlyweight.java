package com.jamith.globemedhms.patterns.flyweight;

import com.jamith.globemedhms.patterns.roleobject.*;

import java.util.HashSet;
import java.util.Set;

public class RoleFlyweight {
    private final String roleName;
    private final Role roleObject; // Reference to the Role object

    public RoleFlyweight(String roleName) {
        this.roleName = roleName;
        // Create the corresponding Role object
        this.roleObject = createRoleObject(roleName);
    }

    private Role createRoleObject(String roleName) {
        return switch (roleName.toUpperCase()) {
            case "DOCTOR" -> new DoctorRole();
            case "NURSE" -> new NurseRole();
            case "PHARMACIST" -> new PharmacistRole();
            case "ADMIN" -> new AdminRole();
            default -> throw new IllegalArgumentException("Unknown role: " + roleName);
        };
    }

    public boolean hasPermission(String permission) {
        return roleObject.hasPermission(permission);
    }

    public String getRoleName() {
        return roleName;
    }

    public Role getRoleObject() {
        return roleObject;
    }
}