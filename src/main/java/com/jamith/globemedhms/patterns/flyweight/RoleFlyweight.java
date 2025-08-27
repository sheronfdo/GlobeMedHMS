package com.jamith.globemedhms.patterns.flyweight;

import java.util.HashSet;
import java.util.Set;

public class RoleFlyweight {
    private final String roleName;
    private final Set<String> permissions = new HashSet<>();

    public RoleFlyweight(String roleName) {
        this.roleName = roleName;
        if ("DOCTOR".equals(roleName)) {
            permissions.add("VIEW_PATIENT_RECORDS");
            permissions.add("UPDATE_TREATMENT_PLANS");
            permissions.add("PRESCRIBE_MEDICATIONS");
        } else if ("NURSE".equals(roleName)) {
            permissions.add("VIEW_PATIENT_RECORDS");
            permissions.add("ADMINISTER_MEDICATIONS");
        } else if ("PHARMACIST".equals(roleName)) {
            permissions.add("VIEW_PRESCRIPTIONS");
            permissions.add("DISPENSE_MEDICATIONS");
        } else if ("ADMIN".equals(roleName)) {
            permissions.add("MANAGE_STAFF");
            permissions.add("VIEW_ALL_RECORDS");
            permissions.add("GENERATE_REPORTS");
        }
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public String getRoleName() {
        return roleName;
    }
}