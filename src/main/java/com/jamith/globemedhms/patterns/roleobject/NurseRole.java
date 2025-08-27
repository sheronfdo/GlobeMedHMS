package com.jamith.globemedhms.patterns.roleobject;

import java.util.HashSet;
import java.util.Set;

public class NurseRole implements Role {
    private final Set<String> permissions = new HashSet<>();

    public NurseRole() {
        permissions.add("VIEW_PATIENT_RECORDS");
        permissions.add("ADMINISTER_MEDICATIONS");
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