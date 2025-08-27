package com.jamith.globemedhms.patterns.roleobject;

import java.util.Set;

public interface Role {
    boolean hasPermission(String permission);
    Set<String> getPermissions();
}