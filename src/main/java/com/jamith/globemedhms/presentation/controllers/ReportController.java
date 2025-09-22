package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.report.PdfReportService;
import com.jamith.globemedhms.core.entities.*;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.patterns.visitor.*;
import com.jamith.globemedhms.presentation.views.report.ReportView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportController {
    private final ReportView view;
    private final ResourceProxy proxy = new ResourceProxy();

    public ReportController(ReportView view, Staff loggedInStaff) {
        this.view = view;
        view.addGenerateReportListener(new GenerateReportListener(loggedInStaff));
        view.addExportPdfListener(new ExportPdfListener(loggedInStaff));
        view.addShowChartListener(new ShowAnalyticsListener(loggedInStaff));
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

                ReportVisitor visitor = createVisitor(view.getReportType());
                if (visitor == null) {
                    view.showMessage("Invalid report type.");
                    return;
                }

                String report = generateReport(selectedEntity, visitor, view.getEntityType());
                view.setReportOutput(report);

            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }

        private ReportVisitor createVisitor(String reportType) {
            switch (reportType) {
                case "Treatment Summary": return new TreatmentSummaryVisitor();
                case "Diagnostic Results": return new DiagnosticResultsVisitor();
                case "Financial Report": return new FinancialReportVisitor();
                case "Staff Performance": return new StaffPerformanceVisitor();
                default: return null;
            }
        }

        private String generateReport(Object entity, ReportVisitor visitor, String entityType) {
            switch (entityType) {
                case "Patient": return ((Patient) entity).accept(visitor);
                case "Appointment": return ((Appointment) entity).accept(visitor);
                case "Billing": return ((Billing) entity).accept(visitor);
                case "Insurance Claim": return ((InsuranceClaim) entity).accept(visitor);
                case "Staff": return ((Staff) entity).accept(visitor);
                default: return "Invalid entity type.";
            }
        }
    }

    class ExportPdfListener implements ActionListener {
        private final Staff staff;

        public ExportPdfListener(Staff staff) {
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

                String reportType = view.getReportType();
                String filename = generatePdfReport(selectedEntity, reportType);

                if (filename != null) {
                    view.showMessage("PDF report generated: " + filename);
                    view.openPdfFile(filename);
                } else {
                    view.showMessage("Failed to generate PDF report.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }

        private String generatePdfReport(Object entity, String reportType) {
            if (entity instanceof Patient) {
                return PdfReportService.generatePatientReport((Patient) entity, reportType);
            } else if (entity instanceof Appointment) {
                return PdfReportService.generateAppointmentReport((Appointment) entity, reportType);
            } else if (entity instanceof Billing) {
                return PdfReportService.generateBillingReport((Billing) entity, reportType);
            } else if (entity instanceof InsuranceClaim) {
                return PdfReportService.generateInsuranceClaimReport((InsuranceClaim) entity, reportType);
            }
            return null;
        }
    }

    class ShowAnalyticsListener implements ActionListener {
        private final Staff staff;

        public ShowAnalyticsListener(Staff staff) {
            this.staff = staff;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(staff, "REPORTS", "GENERATE_REPORTS");
                // Placeholder for analytics functionality
                view.showMessage("Analytics feature coming soon!");
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }
}
