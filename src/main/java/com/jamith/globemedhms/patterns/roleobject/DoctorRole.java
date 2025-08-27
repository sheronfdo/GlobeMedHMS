package com.jamith.globemedhms.patterns.roleobject;

import java.util.HashSet;
import java.util.Set;

public class DoctorRole implements Role {
    private final Set<String> permissions = new HashSet<>();

    public DoctorRole() {
        permissions.add("VIEW_PATIENT_RECORDS");
        permissions.add("UPDATE_PATIENT_RECORDS");
        permissions.add("UPDATE_TREATMENT_PLANS");
        permissions.add("PRESCRIBE_MEDICATIONS");
        permissions.add("MANAGE_APPOINTMENTS");
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    @Override
    public Set<String> getPermissions() {
        return permissions;
    }
}