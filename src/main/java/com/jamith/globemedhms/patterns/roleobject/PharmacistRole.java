package com.jamith.globemedhms.patterns.roleobject;

import java.util.HashSet;
import java.util.Set;

public class PharmacistRole implements Role {
    private final Set<String> permissions = new HashSet<>();

    public PharmacistRole() {
        permissions.add("VIEW_PRESCRIPTIONS");
        permissions.add("DISPENSE_MEDICATIONS");
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