package com.jamith.globemedhms.core.entities;

import com.jamith.globemedhms.patterns.flyweight.RoleFlyweight;
import com.jamith.globemedhms.patterns.roleobject.Role;
import com.jamith.globemedhms.patterns.visitor.ReportVisitor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

//    @Transient
//    private List<Role> roles = new ArrayList<>();

    @Transient
    private RoleFlyweight roleFlyweight;

    @Column(nullable = false)
    private String role;

    public Staff() {
    }

    public Staff(String name, String username, String passwordHash, String role) {
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public void setRoleFlyweight(RoleFlyweight roleFlyweight) {
        this.roleFlyweight = roleFlyweight;
        if (roleFlyweight != null) {
            this.role = roleFlyweight.getRoleName();
        }
    }

    public boolean hasPermission(String permission) {
        return roleFlyweight != null && roleFlyweight.hasPermission(permission);
    }

//    public void addRole(Role role) {
//        roles.add(role);
//        this.role = role.getClass().getSimpleName().replace("Role", "").toUpperCase();
//    }
//
//    public void removeRole(Role role) {
//        roles.remove(role);
//        this.role = roles.isEmpty() ? "UNKNOWN" : roles.get(0).getClass().getSimpleName().replace("Role", "").toUpperCase();
//    }

//    public boolean hasPermission(String permission) {
//        return roles.stream().anyMatch(r -> r.hasPermission(permission));
//    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    //    public List<Role> getRoles() { return roles; }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name + " (" + username + ")";
    }

    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}