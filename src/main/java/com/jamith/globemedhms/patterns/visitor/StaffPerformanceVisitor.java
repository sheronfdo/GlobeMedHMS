package com.jamith.globemedhms.patterns.visitor;

import com.jamith.globemedhms.core.entities.*;
import com.jamith.globemedhms.patterns.composite.ReportComposite;
import com.jamith.globemedhms.patterns.composite.ReportSection;

public class StaffPerformanceVisitor implements ReportVisitor {
    @Override
    public String visit(Patient patient) {
        return "Staff Performance report not applicable for Patient: " + patient.getName();
    }

    @Override
    public String visit(Appointment appointment) {
        return "Staff Performance report not applicable for Appointment ID: " + appointment.getId();
    }

    @Override
    public String visit(Billing billing) {
        return "Staff Performance report not applicable for Billing ID: " + billing.getId();
    }

    @Override
    public String visit(InsuranceClaim claim) {
        return "Staff Performance report not applicable for Insurance Claim ID: " + claim.getId();
    }

    @Override
    public String visit(Staff staff) {
        ReportComposite report = new ReportComposite("Staff Performance Report for " + staff.getName());
        
        ReportSection roleSection = new ReportSection("Role Information");
        roleSection.setContent("Role: " + staff.getRole() + "\nUsername: " + staff.getUsername());
        
        ReportSection permissionsSection = new ReportSection("Permissions");
        permissionsSection.setContent("Has permission to manage patients: " + 
            staff.hasPermission("VIEW_PATIENT_RECORDS") + "\n" +
            "Has permission to manage appointments: " + 
            staff.hasPermission("MANAGE_APPOINTMENTS"));
        
        report.addSection(roleSection);
        report.addSection(permissionsSection);
        
        return report.generate();
    }
}