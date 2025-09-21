package com.jamith.globemedhms.patterns.flyweight;

import java.util.HashMap;
import java.util.Map;

public class RoleFlyweightFactory {
    private static final Map<String, RoleFlyweight> flyweights = new HashMap<>();

    public static RoleFlyweight getRoleFlyweight(String roleName) {
        return flyweights.computeIfAbsent(roleName.toUpperCase(),
                key -> new RoleFlyweight(key));
    }
}