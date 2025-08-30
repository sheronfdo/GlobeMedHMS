package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.patterns.visitor.DiagnosticResultsVisitor;
import com.jamith.globemedhms.patterns.visitor.FinancialReportVisitor;
import com.jamith.globemedhms.patterns.visitor.ReportVisitor;
import com.jamith.globemedhms.patterns.visitor.TreatmentSummaryVisitor;
import com.jamith.globemedhms.presentation.views.report.ReportView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportController {
    private final ReportView view;
    private final ResourceProxy proxy = new ResourceProxy();

    public ReportController(ReportView view, Staff loggedInStaff) {
        this.view = view;
        view.addGenerateReportListener(new GenerateReportListener(loggedInStaff));
    }

    class GenerateReportListener implements ActionListener {
        private final Staff staff;

        public GenerateReportListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "REPORTS", "GENERATE_REPORTS");
                Object selectedEntity = view.getSelectedEntity();
                if (selectedEntity == null) {
                    view.showMessage("Please select an entity.");
                    return;
                }

                ReportVisitor visitor;
                String reportType = view.getReportType();
                switch (reportType) {
                    case "Treatment Summary":
                        visitor = new TreatmentSummaryVisitor();
                        break;
                    case "Diagnostic Results":
                        visitor = new DiagnosticResultsVisitor();
                        break;
                    case "Financial Report":
                        visitor = new FinancialReportVisitor();
                        break;
                    default:
                        view.showMessage("Invalid report type.");
                        return;
                }

                String report;
                String entityType = view.getEntityType();
                switch (entityType) {
                    case "Patient":
                        if (selectedEntity instanceof Patient) {
                            report = ((Patient) selectedEntity).accept(visitor);
                        } else {
                            view.showMessage("Selected entity is not a Patient.");
                            return;
                        }
                        break;
                    case "Appointment":
                        if (selectedEntity instanceof Appointment) {
                            report = ((Appointment) selectedEntity).accept(visitor);
                        } else {
                            view.showMessage("Selected entity is not an Appointment.");
                            return;
                        }
                        break;
                    case "Billing":
                        if (selectedEntity instanceof Billing) {
                            report = ((Billing) selectedEntity).accept(visitor);
                        } else {
                            view.showMessage("Selected entity is not a Billing.");
                            return;
                        }
                        break;
                    default:
                        view.showMessage("Invalid entity type.");
                        return;
                }

                view.setReportOutput(report);
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }
}