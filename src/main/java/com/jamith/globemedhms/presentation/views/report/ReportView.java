package com.jamith.globemedhms.presentation.views.report;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
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
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<String> entityTypeComboBox;
    private JList<Object> entityList;
    private JTextArea reportOutput;
    private JButton generateReportButton;
    private final Staff loggedInStaff;

    public ReportView(Staff loggedInStaff) {
//        this.loggedInStaff = loggedInStaff;
//        setLayout(new BorderLayout(10, 10));
//        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 5, 5));
//        selectionPanel.setBorder(BorderFactory.createTitledBorder("Report Selection"));
//        selectionPanel.add(new JLabel("Report Type:"));
//        reportTypeComboBox = new JComboBox<>(new String[]{"Treatment Summary", "Diagnostic Results", "Financial Report"});
//        selectionPanel.add(reportTypeComboBox);
//        selectionPanel.add(new JLabel("Entity Type:"));
//        entityTypeComboBox = new JComboBox<>(new String[]{"Patient", "Appointment", "Billing"});
//        entityTypeComboBox.addActionListener(e -> updateEntityList());
//        selectionPanel.add(entityTypeComboBox);
//        generateReportButton = new JButton("Generate Report");
//        selectionPanel.add(generateReportButton);
//        add(selectionPanel, BorderLayout.NORTH);
//
//        entityList = new JList<>();
//        updateEntityList();
//        JScrollPane entityListScroll = new JScrollPane(entityList);
//        add(entityListScroll, BorderLayout.WEST);
//
//        reportOutput = new JTextArea(20, 40);
//        reportOutput.setEditable(false);
//        JScrollPane outputScroll = new JScrollPane(reportOutput);
//        add(outputScroll, BorderLayout.CENTER);
//
//        new ReportController(this, loggedInStaff);


        this.loggedInStaff = loggedInStaff;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 🔹 Top Panel - Report Selection
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Report Selection"));

        selectionPanel.add(new JLabel("Report Type:"));
        reportTypeComboBox = new JComboBox<>(new String[]{"Treatment Summary", "Diagnostic Results", "Financial Report"});
        selectionPanel.add(reportTypeComboBox);

        selectionPanel.add(new JLabel("Entity Type:"));
        entityTypeComboBox = new JComboBox<>(new String[]{"Patient", "Appointment", "Billing"});
        entityTypeComboBox.addActionListener(e -> updateEntityList());
        selectionPanel.add(entityTypeComboBox);

        generateReportButton = new JButton("📊 Generate Report");
        generateReportButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        generateReportButton.setBackground(new Color(52, 152, 219));
        generateReportButton.setForeground(Color.WHITE);
        generateReportButton.setFocusPainted(false);
        generateReportButton.setPreferredSize(new Dimension(200, 35));
        selectionPanel.add(new JLabel()); // filler for alignment
        selectionPanel.add(generateReportButton);

        add(selectionPanel, BorderLayout.NORTH);

        // 🔹 Left Panel - Entity List
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Entities"));

        entityList = new JList<>();
        updateEntityList();
        entityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entityList.setVisibleRowCount(12);

        JScrollPane entityListScroll = new JScrollPane(entityList);
        listPanel.add(entityListScroll, BorderLayout.CENTER);

        add(listPanel, BorderLayout.WEST);

        // 🔹 Center Panel - Report Output
        JPanel outputPanel = new JPanel(new BorderLayout(10, 10));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Generated Report"));

        reportOutput = new JTextArea(20, 40);
        reportOutput.setEditable(false);
        reportOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportOutput.setLineWrap(true);
        reportOutput.setWrapStyleWord(true);

        JScrollPane outputScroll = new JScrollPane(reportOutput);
        outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        outputPanel.add(outputScroll, BorderLayout.CENTER);

        add(outputPanel, BorderLayout.CENTER);

        // 🔹 Hook controller
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
    }

    public void addGenerateReportListener(ActionListener listener) {
        generateReportButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}