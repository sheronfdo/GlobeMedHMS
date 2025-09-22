package com.jamith.globemedhms.presentation.views.report;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.application.services.insurance.InsuranceClaimService;
import com.jamith.globemedhms.application.services.insurance.InsuranceClaimServiceImpl;
import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.application.services.staff.StaffService;
import com.jamith.globemedhms.application.services.staff.StaffServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Billing;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.visitor.DiagnosticResultsVisitor;
import com.jamith.globemedhms.patterns.visitor.FinancialReportVisitor;
import com.jamith.globemedhms.patterns.visitor.TreatmentSummaryVisitor;
import com.jamith.globemedhms.presentation.controllers.ReportController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class ReportView extends JPanel {
    private final PatientService patientService = new PatientServiceImpl();
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private final BillingService billingService = new BillingServiceImpl();
    private final InsuranceClaimService claimService = new InsuranceClaimServiceImpl();
    private final StaffService staffService = new StaffServiceImpl();

    private JComboBox<String> reportTypeComboBox;
    private JComboBox<String> entityTypeComboBox;
    private JList<Object> entityList;
    private JTextArea reportOutput;
    private JButton generateReportButton;
    private JButton exportPdfButton;
    private JButton showChartButton;
    private JTabbedPane tabbedPane;
    private final Staff loggedInStaff;

    public ReportView(Staff loggedInStaff) {
        this.loggedInStaff = loggedInStaff;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Panel - Report Selection
        JPanel selectionPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Report Configuration"));

        selectionPanel.add(new JLabel("Report Type:"));
        reportTypeComboBox = new JComboBox<>(new String[]{
                "Treatment Summary", "Diagnostic Results", "Financial Report", "Staff Performance"
        });
        selectionPanel.add(reportTypeComboBox);

        selectionPanel.add(new JLabel("Entity Type:"));
        entityTypeComboBox = new JComboBox<>(new String[]{
                "Patient", "Appointment", "Billing", "Insurance Claim", "Staff"
        });
        entityTypeComboBox.addActionListener(e -> updateEntityList());
        selectionPanel.add(entityTypeComboBox);

        generateReportButton = new JButton("📊 Generate Report");
        generateReportButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        generateReportButton.setBackground(new Color(52, 152, 219));
        generateReportButton.setForeground(Color.WHITE);
        generateReportButton.setFocusPainted(false);
        selectionPanel.add(generateReportButton);

        exportPdfButton = new JButton("📄 Export to PDF");
        exportPdfButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportPdfButton.setBackground(new Color(231, 76, 60));
        exportPdfButton.setForeground(Color.WHITE);
        exportPdfButton.setFocusPainted(false);
        exportPdfButton.setEnabled(false);
        selectionPanel.add(exportPdfButton);

        showChartButton = new JButton("📈 Show Analytics");
        showChartButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        showChartButton.setBackground(new Color(46, 204, 113));
        showChartButton.setForeground(Color.WHITE);
        showChartButton.setFocusPainted(false);
        selectionPanel.add(showChartButton);

        selectionPanel.add(new JLabel()); // filler

        add(selectionPanel, BorderLayout.NORTH);

        // Center Panel - Tabbed pane
        tabbedPane = new JTabbedPane();

        // Left Panel - Entity List
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Select Entity"));

        entityList = new JList<>();
        updateEntityList();
        entityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entityList.setVisibleRowCount(15);
        entityList.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane entityListScroll = new JScrollPane(entityList);
        listPanel.add(entityListScroll, BorderLayout.CENTER);

        // Report Output Panel
        JPanel reportPanel = new JPanel(new BorderLayout(10, 10));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Generated Report"));

        reportOutput = new JTextArea(25, 50);
        reportOutput.setEditable(false);
        reportOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportOutput.setLineWrap(true);
        reportOutput.setWrapStyleWord(true);

        JScrollPane outputScroll = new JScrollPane(reportOutput);
        outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        reportPanel.add(outputScroll, BorderLayout.CENTER);

        tabbedPane.addTab("📋 Report", reportPanel);
        tabbedPane.addTab("📊 Analytics", new JPanel()); // Empty initially

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPanel, tabbedPane);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);

        add(splitPane, BorderLayout.CENTER);

        // Hook controller
        new ReportController(this, loggedInStaff);
    }

    public void updateEntityList() {
        String entityType = (String) entityTypeComboBox.getSelectedItem();
        if (entityType == null) return;

        switch (entityType) {
            case "Patient":
                entityList.setListData(patientService.getAllPatients().toArray());
                break;
            case "Appointment":
                entityList.setListData(appointmentService.getAllAppointments().toArray());
                break;
            case "Billing":
                entityList.setListData(billingService.getAllBillings().toArray());
                break;
            case "Insurance Claim":
                entityList.setListData(claimService.getAllClaims().toArray());
                break;
            case "Staff":
                entityList.setListData(staffService.getAllStaff().toArray());
                break;
        }
    }

    public String getReportType() {
        return (String) reportTypeComboBox.getSelectedItem();
    }

    public String getEntityType() {
        return (String) entityTypeComboBox.getSelectedItem();
    }

    public Object getSelectedEntity() {
        return entityList.getSelectedValue();
    }

    public void setReportOutput(String text) {
        reportOutput.setText(text);
        exportPdfButton.setEnabled(true);
    }

    public void addGenerateReportListener(ActionListener listener) {
        generateReportButton.addActionListener(listener);
    }

    public void addExportPdfListener(ActionListener listener) {
        exportPdfButton.addActionListener(listener);
    }

    public void addShowChartListener(ActionListener listener) {
        showChartButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showAnalytics(JPanel analyticsPanel) {
        tabbedPane.setComponentAt(1, analyticsPanel);
        tabbedPane.setSelectedIndex(1);
    }

    public void openPdfFile(String filename) {
        try {
            java.awt.Desktop.getDesktop().open(new java.io.File(filename));
        } catch (Exception e) {
            showMessage("Error opening PDF: " + e.getMessage());
        }
    }
}