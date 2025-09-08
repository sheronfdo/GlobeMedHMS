package com.jamith.globemedhms.presentation.views.staff;

import com.jamith.globemedhms.application.services.staff.StaffService;
import com.jamith.globemedhms.application.services.staff.StaffServiceImpl;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.presentation.controllers.StaffController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class StaffView extends JPanel {
    private final StaffService service = new StaffServiceImpl();
    private JList<Staff> staffList;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton addStaffButton;
    private JButton assignRoleButton;
    private JButton revokeRoleButton;
    private JButton accessResourceButton;

    public StaffView() {
//        setLayout(new BorderLayout(10, 10));
//        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        staffList = new JList<>();
//        updateStaffList();
//        JScrollPane staffListScroll = new JScrollPane(staffList);
//        add(staffListScroll, BorderLayout.CENTER);
//
//        JPanel addPanel = new JPanel(new GridLayout(4, 2, 5, 5));
//        addPanel.setBorder(BorderFactory.createTitledBorder("Add New Staff"));
//        addPanel.add(new JLabel("Name:"));
//        nameField = new JTextField(20);
//        addPanel.add(nameField);
//        addPanel.add(new JLabel("Username:"));
//        usernameField = new JTextField(20);
//        addPanel.add(usernameField);
//        addPanel.add(new JLabel("Password:"));
//        passwordField = new JPasswordField(20);
//        addPanel.add(passwordField);
//        addStaffButton = new JButton("Add Staff");
//        addPanel.add(new JLabel(""));
//        addPanel.add(addStaffButton);
//        add(addPanel, BorderLayout.NORTH);
//
//        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
//        rolePanel.setBorder(BorderFactory.createTitledBorder("Role Management"));
//        rolePanel.add(new JLabel("Select Role:"));
//        roleComboBox = new JComboBox<>(new String[]{"DOCTOR", "NURSE", "PHARMACIST", "ADMIN"});
//        rolePanel.add(roleComboBox);
//        assignRoleButton = new JButton("Assign Role");
//        rolePanel.add(assignRoleButton);
//        revokeRoleButton = new JButton("Revoke Role");
//        rolePanel.add(revokeRoleButton);
//        accessResourceButton = new JButton("Test Access to Patient Records");
//        rolePanel.add(accessResourceButton);
//        add(rolePanel, BorderLayout.SOUTH);
//
//        new StaffController(this);



        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ===== Staff List Panel =====
        staffList = new JList<>();
        updateStaffList();
        staffList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffList.setBorder(BorderFactory.createTitledBorder("Staff Members"));
        JScrollPane staffListScroll = new JScrollPane(staffList);
        staffListScroll.setPreferredSize(new Dimension(250, 400));
        add(staffListScroll, BorderLayout.WEST);

        // ===== Center Panel for Add & Role Management =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Add Staff Form
        JPanel addPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add New Staff"));

        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        addStaffButton = new JButton("➕ Add Staff");

        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Username:"));
        addPanel.add(usernameField);
        addPanel.add(new JLabel("Password:"));
        addPanel.add(passwordField);
        addPanel.add(new JLabel(""));
        addPanel.add(addStaffButton);

        centerPanel.add(addPanel, BorderLayout.NORTH);

        // Role Management
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rolePanel.setBorder(BorderFactory.createTitledBorder("Role Management"));

        roleComboBox = new JComboBox<>(new String[]{"DOCTOR", "NURSE", "PHARMACIST", "ADMIN"});
        assignRoleButton = new JButton("✅ Assign Role");
        revokeRoleButton = new JButton("❌ Revoke Role");
        accessResourceButton = new JButton("🔎 Test Access to Patient Records");

        rolePanel.add(new JLabel("Select Role:"));
        rolePanel.add(roleComboBox);
        rolePanel.add(assignRoleButton);
        rolePanel.add(revokeRoleButton);
        rolePanel.add(accessResourceButton);

        centerPanel.add(rolePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Attach Controller
        new StaffController(this);


    }

    public void updateStaffList() {
        List<Staff> staff = service.getAllStaff();
        staffList.setListData(staff.toArray(new Staff[0]));
    }

    public Staff getSelectedStaff() {
        return staffList.getSelectedValue();
    }

    public String getSelectedRole() {
        return (String) roleComboBox.getSelectedItem();
    }

    public String getNewStaffName() {
        return nameField.getText();
    }

    public String getNewStaffUsername() {
        return usernameField.getText();
    }

    public String getNewStaffPassword() {
        return new String(passwordField.getPassword());
    }

    public void addAddStaffListener(ActionListener listener) {
        addStaffButton.addActionListener(listener);
    }

    public void addAssignRoleListener(ActionListener listener) {
        assignRoleButton.addActionListener(listener);
    }

    public void addRevokeRoleListener(ActionListener listener) {
        revokeRoleButton.addActionListener(listener);
    }

    public void addAccessResourceListener(ActionListener listener) {
        accessResourceButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}