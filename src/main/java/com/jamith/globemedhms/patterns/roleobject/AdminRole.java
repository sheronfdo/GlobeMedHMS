package com.jamith.globemedhms.patterns.roleobject;

import java.util.HashSet;
import java.util.Set;

public class AdminRole implements Role {
    private final Set<String> permissions = new HashSet<>();

    public AdminRole() {
        permissions.add("MANAGE_STAFF");
        permissions.add("VIEW_ALL_RECORDS");
        permissions.add("GENERATE_REPORTS");
        permissions.add("VIEW_PATIENT_RECORDS");
        permissions.add("UPDATE_PATIENT_RECORDS");
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