package com.jamith.globemedhms.presentation.views;

import com.jamith.globemedhms.application.services.staff.StaffService;
import com.jamith.globemedhms.application.services.staff.StaffServiceImpl;
import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.AuditLogRepository;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.appointment.AppointmentView;
import com.jamith.globemedhms.presentation.views.nurse.AdministerMedView;
import com.jamith.globemedhms.presentation.views.patient.PatientView;
import com.jamith.globemedhms.presentation.views.staff.StaffView;
import com.jamith.globemedhms.presentation.views.pharmacist.ViewPrescriptionsView;
import com.jamith.globemedhms.presentation.views.pharmacist.DispenseMedView;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final StaffService service = new StaffServiceImpl();
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();
    private final ResourceProxy proxy = new ResourceProxy();
    private Staff loggedInStaff;

    public MainFrame() {
        setTitle("GlobeMed Healthcare System");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
        showLoginPanel();
        setVisible(true);
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (service.authenticate(username, password)) {
                loggedInStaff = service.getLoggedInStaff(username);
                auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Logged in", LocalDateTime.now()));
                showDashboardPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        mainPanel.add(loginPanel, "LOGIN");
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void showDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Dashboard - " + loggedInStaff.getRole(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel sidebar = new JPanel(new GridLayout(5, 1, 5, 5));
        sidebar.setBorder(BorderFactory.createTitledBorder("Navigation"));
        dashboardPanel.add(sidebar, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new CardLayout());
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        // Role-based sidebar and content
        String role = loggedInStaff.getRole();
        addSidebarButtons(role, sidebar, contentPanel);

        mainPanel.add(dashboardPanel, "DASHBOARD");
        cardLayout.show(mainPanel, "DASHBOARD");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void addSidebarButtons(String role, JPanel sidebar, JPanel contentPanel) {
        switch (role) {
            case "ADMIN" -> {
                JButton manageStaffButton = new JButton("Manage Staff");
                manageStaffButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "STAFF_MANAGEMENT", "MANAGE_STAFF");
                        StaffView staffView = new StaffView();
                        contentPanel.add(staffView, "MANAGE_STAFF");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "MANAGE_STAFF");
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Accessed Manage Staff", LocalDateTime.now()));
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(manageStaffButton);

                JButton manageAppointmentsButton = new JButton("Manage Appointments");
                manageAppointmentsButton.addActionListener(e -> {
                    proxy.accessResource(loggedInStaff, "APPOINTMENT_MANAGEMENT", "MANAGE_APPOINTMENTS");
                    AppointmentView appointmentView = new AppointmentView(loggedInStaff);
                    contentPanel.add(appointmentView, "MANAGE_APPOINTMENTS");
                    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "MANAGE_APPOINTMENTS");
                });
                sidebar.add(manageAppointmentsButton);

                JButton managePatientsButton = new JButton("Manage Patients");
                managePatientsButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "PATIENT_RECORDS", "VIEW_ALL_RECORDS");
                        PatientView patientView = new PatientView(loggedInStaff);
                        contentPanel.add(patientView, "MANAGE_PATIENTS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "MANAGE_PATIENTS");
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(managePatientsButton);

                JButton viewReportsButton = new JButton("View Reports");
                viewReportsButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "REPORTS", "GENERATE_REPORTS");
                        ReportView reportView = new ReportView();
                        contentPanel.add(reportView, "VIEW_REPORTS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "VIEW_REPORTS");
                        contentPanel.revalidate();
                        contentPanel.repaint();
                        auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Accessed View Reports", LocalDateTime.now()));
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(viewReportsButton);

                JButton logoutButton = new JButton("Logout");
                logoutButton.addActionListener(e -> {
                    auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Logged out", LocalDateTime.now()));
                    cardLayout.show(mainPanel, "LOGIN");
                });
                sidebar.add(logoutButton);
            }
            case "DOCTOR" -> {
                JButton manageAppointmentsButton = new JButton("Manage Appointments");
                manageAppointmentsButton.addActionListener(e -> {
                    proxy.accessResource(loggedInStaff, "APPOINTMENT_MANAGEMENT", "MANAGE_APPOINTMENTS");
                    AppointmentView appointmentView = new AppointmentView(loggedInStaff);
                    contentPanel.add(appointmentView, "MANAGE_APPOINTMENTS");
                    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "MANAGE_APPOINTMENTS");
                });
                sidebar.add(manageAppointmentsButton);

                JButton viewPatientsButton = new JButton("View Patients");
                viewPatientsButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "PATIENT_RECORDS", "VIEW_PATIENT_RECORDS");
                        PatientView patientView = new PatientView(loggedInStaff);
                        contentPanel.add(patientView, "VIEW_PATIENTS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "VIEW_PATIENTS");
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(viewPatientsButton);

                JButton prescribeMedButton = new JButton("Prescribe Medications");
                prescribeMedButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "PRESCRIPTION", "PRESCRIBE_MEDICATIONS");
                        AppointmentView appointmentView = new AppointmentView(loggedInStaff);
                        contentPanel.add(appointmentView, "PRESCRIBE_MEDICATIONS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "PRESCRIBE_MEDICATIONS");
                        auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Accessed Prescribe Medications", LocalDateTime.now()));
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(prescribeMedButton);

                JButton logoutButton = new JButton("Logout");
                logoutButton.addActionListener(e -> {
                    auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Logged out", LocalDateTime.now()));
                    cardLayout.show(mainPanel, "LOGIN");
                });
                sidebar.add(logoutButton);
            }
            case "NURSE" -> {
                JButton manageAppointmentsButton = new JButton("Manage Appointments");
                manageAppointmentsButton.addActionListener(e -> {
                    proxy.accessResource(loggedInStaff, "APPOINTMENT_MANAGEMENT", "MANAGE_APPOINTMENTS");
                    AppointmentView appointmentView = new AppointmentView(loggedInStaff);
                    contentPanel.add(appointmentView, "MANAGE_APPOINTMENTS");
                    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "MANAGE_APPOINTMENTS");
                });
                sidebar.add(manageAppointmentsButton);

                JButton viewPatientsButton = new JButton("View Patients");
                viewPatientsButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "PATIENT_RECORDS", "VIEW_PATIENT_RECORDS");
                        PatientView patientView = new PatientView(loggedInStaff);
                        contentPanel.add(patientView, "VIEW_PATIENTS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "VIEW_PATIENTS");
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(viewPatientsButton);

                JButton administerMedButton = new JButton("Administer Medications");
                administerMedButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "ADMINISTER_MEDICATIONS", "ADMINISTER_MEDICATIONS");
                        AdministerMedView administerMedView = new AdministerMedView(loggedInStaff);
                        contentPanel.add(administerMedView, "ADMINISTER_MEDICATIONS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ADMINISTER_MEDICATIONS");
                        auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Accessed Administer Medications", LocalDateTime.now()));
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(administerMedButton);

                JButton logoutButton = new JButton("Logout");
                logoutButton.addActionListener(e -> {
                    auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Logged out", LocalDateTime.now()));
                    cardLayout.show(mainPanel, "LOGIN");
                });
                sidebar.add(logoutButton);
            }
            case "PHARMACIST" -> {
                JButton viewPrescriptionsButton = new JButton("View Prescriptions");
                viewPrescriptionsButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "PRESCRIPTIONS", "VIEW_PRESCRIPTIONS");
                        ViewPrescriptionsView viewPrescriptionsView = new ViewPrescriptionsView(loggedInStaff);
                        contentPanel.add(viewPrescriptionsView, "VIEW_PRESCRIPTIONS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "VIEW_PRESCRIPTIONS");
                        auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Accessed View Prescriptions", LocalDateTime.now()));
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(viewPrescriptionsButton);

                JButton dispenseMedButton = new JButton("Dispense Medications");
                dispenseMedButton.addActionListener(e -> {
                    try {
                        proxy.accessResource(loggedInStaff, "DISPENSE_MEDICATIONS", "DISPENSE_MEDICATIONS");
                        DispenseMedView dispenseMedView = new DispenseMedView(loggedInStaff);
                        contentPanel.add(dispenseMedView, "DISPENSE_MEDICATIONS");
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "DISPENSE_MEDICATIONS");
                        auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Accessed Dispense Medications", LocalDateTime.now()));
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                sidebar.add(dispenseMedButton);


                JButton logoutButton = new JButton("Logout");
                logoutButton.addActionListener(e -> {
                    auditLogRepository.save(new AuditLog(loggedInStaff.getId(), "Logged out", LocalDateTime.now()));
                    cardLayout.show(mainPanel, "LOGIN");
                });
                sidebar.add(logoutButton);
            }
            default -> {
                JLabel message = new JLabel("Unknown role. Please contact admin.", SwingConstants.CENTER);
                contentPanel.add(message, "UNKNOWN");
                ((CardLayout) contentPanel.getLayout()).show(contentPanel, "UNKNOWN");
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        }
    }

    private void showPlaceholder(JPanel contentPanel, String message) {
        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.add(placeholder, message.toUpperCase().replace(" ", "_"));
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, message.toUpperCase().replace(" ", "_"));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
