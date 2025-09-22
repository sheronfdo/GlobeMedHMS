package com.jamith.globemedhms.application.services.analytics;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.application.services.billing.BillingService;
import com.jamith.globemedhms.application.services.billing.BillingServiceImpl;
import com.jamith.globemedhms.application.services.patient.PatientService;
import com.jamith.globemedhms.application.services.patient.PatientServiceImpl;
import com.jamith.globemedhms.core.entities.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.*;

public class AnalyticsService {
    private static final Logger logger = LogManager.getLogger(AnalyticsService.class);
    
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private final BillingService billingService = new BillingServiceImpl();
    private final PatientService patientService = new PatientServiceImpl();

    // Appointment Analytics
    public JPanel createAppointmentStatusChart() {
        try {
            List<Object[]> statusData = appointmentService.getAppointmentStatusCount();
            DefaultPieDataset dataset = new DefaultPieDataset();
            
            for (Object[] row : statusData) {
                String status = (String) row[0];
                Long count = (Long) row[1];
                dataset.setValue(status + " (" + count + ")", count);
            }
            
            JFreeChart chart = ChartFactory.createPieChart(
                "Appointment Status Distribution",
                dataset,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating appointment status chart", e);
            return createErrorPanel("Failed to load appointment analytics");
        }
    }

    public JPanel createAppointmentTypeChart() {
        try {
            List<Object[]> typeData = appointmentService.getAppointmentTypeCount();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            for (Object[] row : typeData) {
                String type = (String) row[0];
                Long count = (Long) row[1];
                dataset.addValue(count, "Appointments", type);
            }
            
            JFreeChart chart = ChartFactory.createBarChart(
                "Appointment Types Distribution",
                "Appointment Type",
                "Number of Appointments",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating appointment type chart", e);
            return createErrorPanel("Failed to load appointment type analytics");
        }
    }

    public JPanel createMonthlyAppointmentTrend() {
        try {
            List<Object[]> trendData = appointmentService.getMonthlyAppointmentTrend();
            TimeSeries series = new TimeSeries("Monthly Appointments");
            
            for (Object[] row : trendData) {
                String monthYear = (String) row[0];
                Long count = (Long) row[1];
                
                // Parse month-year string (format: "2024-01")
                String[] parts = monthYear.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                
                LocalDate localDate = LocalDate.of(year, month, 1);
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                series.add(new Day(date), count);
            }
            
            TimeSeriesCollection dataset = new TimeSeriesCollection(series);
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Monthly Appointment Trends",
                "Month",
                "Number of Appointments",
                dataset,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating monthly appointment trend chart", e);
            return createErrorPanel("Failed to load appointment trend analytics");
        }
    }

    // Billing Analytics
    public JPanel createRevenueChart() {
        try {
            List<Object[]> revenueData = billingService.getMonthlyRevenue();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            for (Object[] row : revenueData) {
                String month = (String) row[0];
                Double revenue = (Double) row[1];
                dataset.addValue(revenue, "Revenue", month);
            }
            
            JFreeChart chart = ChartFactory.createBarChart(
                "Monthly Revenue Analysis",
                "Month",
                "Revenue ($)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating revenue chart", e);
            return createErrorPanel("Failed to load revenue analytics");
        }
    }

    public JPanel createBillingStatusChart() {
        try {
            List<Object[]> statusData = billingService.getBillingStatusDistribution();
            DefaultPieDataset dataset = new DefaultPieDataset();
            
            for (Object[] row : statusData) {
                String status = (String) row[0];
                Long count = (Long) row[1];
                dataset.setValue(status + " (" + count + ")", count);
            }
            
            JFreeChart chart = ChartFactory.createPieChart(
                "Billing Status Distribution",
                dataset,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating billing status chart", e);
            return createErrorPanel("Failed to load billing status analytics");
        }
    }

    public JPanel createInsuranceVsDirectBillingChart() {
        try {
            List<Object[]> typeData = billingService.getBillingTypeDistribution();
            DefaultPieDataset dataset = new DefaultPieDataset();
            
            for (Object[] row : typeData) {
                String type = (String) row[0];
                Long count = (Long) row[1];
                dataset.setValue(type + " (" + count + ")", count);
            }
            
            JFreeChart chart = ChartFactory.createPieChart(
                "Insurance vs Direct Billing",
                dataset,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating billing type chart", e);
            return createErrorPanel("Failed to load billing type analytics");
        }
    }

    // Patient Analytics
    public JPanel createPatientAgeDistributionChart() {
        try {
            List<Object[]> ageData = patientService.getPatientAgeDistribution();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            for (Object[] row : ageData) {
                String ageGroup = (String) row[0];
                Long count = (Long) row[1];
                dataset.addValue(count, "Patients", ageGroup);
            }
            
            JFreeChart chart = ChartFactory.createBarChart(
                "Patient Age Distribution",
                "Age Group",
                "Number of Patients",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating patient age distribution chart", e);
            return createErrorPanel("Failed to load patient age analytics");
        }
    }

    public JPanel createPatientRegistrationTrend() {
        try {
            List<Object[]> registrationData = patientService.getMonthlyRegistrationTrend();
            TimeSeries series = new TimeSeries("Patient Registrations");
            
            for (Object[] row : registrationData) {
                String monthYear = (String) row[0];
                Long count = (Long) row[1];
                
                String[] parts = monthYear.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                
                LocalDate localDate = LocalDate.of(year, month, 1);
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                series.add(new Day(date), count);
            }
            
            TimeSeriesCollection dataset = new TimeSeriesCollection(series);
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Monthly Patient Registration Trends",
                "Month",
                "Number of Registrations",
                dataset,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating patient registration trend chart", e);
            return createErrorPanel("Failed to load patient registration analytics");
        }
    }

    // Staff Performance Analytics
    public JPanel createStaffAppointmentChart() {
        try {
            List<Object[]> staffData = appointmentService.getStaffAppointmentCount();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            for (Object[] row : staffData) {
                String staffName = (String) row[0];
                Long count = (Long) row[1];
                dataset.addValue(count, "Appointments", staffName);
            }
            
            JFreeChart chart = ChartFactory.createBarChart(
                "Staff Appointment Load",
                "Staff Member",
                "Number of Appointments",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            return chartPanel;
        } catch (Exception e) {
            logger.error("Error creating staff appointment chart", e);
            return createErrorPanel("Failed to load staff performance analytics");
        }
    }

    // Statistical Summary Panel
    public JPanel createStatisticalSummary() {
        JPanel summaryPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Key Performance Indicators"));
        
        try {
            // Total Patients
            long totalPatients = patientService.getTotalPatientCount();
            summaryPanel.add(createMetricPanel("Total Patients", String.valueOf(totalPatients), Color.BLUE));
            
            // Total Appointments
            long totalAppointments = appointmentService.getTotalAppointmentCount();
            summaryPanel.add(createMetricPanel("Total Appointments", String.valueOf(totalAppointments), Color.GREEN));
            
            // Total Revenue
            double totalRevenue = billingService.getTotalRevenue();
            summaryPanel.add(createMetricPanel("Total Revenue", String.format("$%.2f", totalRevenue), Color.ORANGE));
            
            // Average Appointment Value
            double avgAppointmentValue = totalRevenue / Math.max(totalAppointments, 1);
            summaryPanel.add(createMetricPanel("Avg Appointment Value", String.format("$%.2f", avgAppointmentValue), Color.MAGENTA));
            
            // Completed Appointments
            long completedAppointments = appointmentService.getCompletedAppointmentCount();
            double completionRate = (double) completedAppointments / Math.max(totalAppointments, 1) * 100;
            summaryPanel.add(createMetricPanel("Completion Rate", String.format("%.1f%%", completionRate), Color.CYAN));
            
            // Insurance Claim Rate
            long insuranceBills = billingService.getInsuranceBillingCount();
            long totalBills = billingService.getTotalBillingCount();
            double insuranceRate = (double) insuranceBills / Math.max(totalBills, 1) * 100;
            summaryPanel.add(createMetricPanel("Insurance Billing Rate", String.format("%.1f%%", insuranceRate), Color.PINK));
            
        } catch (Exception e) {
            logger.error("Error creating statistical summary", e);
            summaryPanel.add(new JLabel("Error loading statistics"));
        }
        
        JScrollPane scrollPane = new JScrollPane(summaryPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JPanel panel2 = new JPanel();
        panel2.add(scrollPane);
        return panel2;
    }

    private JPanel createMetricPanel(String title, String value, Color color) {
        JPanel metricPanel = new JPanel(new BorderLayout());
        metricPanel.setBorder(BorderFactory.createLineBorder(color, 2));
        metricPanel.setBackground(new Color(240, 240, 240));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(color.darker());
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(color);
        
        metricPanel.add(titleLabel, BorderLayout.NORTH);
        metricPanel.add(valueLabel, BorderLayout.CENTER);
        
        return metricPanel;
    }

    private JPanel createErrorPanel(String message) {
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);
        errorPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        return errorPanel;
    }
}