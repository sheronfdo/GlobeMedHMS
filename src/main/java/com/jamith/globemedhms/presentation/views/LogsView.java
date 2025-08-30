package com.jamith.globemedhms.presentation.views;

import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.infrastructure.repository.AuditLogRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.jamith.globemedhms.infrastructure.config.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LogsView extends JPanel {
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();

    public LogsView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("System Activity Reports", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> logModel = new DefaultListModel<>();
        JList<String> logList = new JList<>(logModel);
        JScrollPane logScrollPane = new JScrollPane(logList);
        add(logScrollPane, BorderLayout.CENTER);

        // Fetch and display audit logs
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AuditLog> query = session.createQuery("FROM AuditLog ORDER BY timestamp DESC", AuditLog.class);
            List<AuditLog> logs = query.getResultList();
            for (AuditLog log : logs) {
                logModel.addElement("Staff ID: " + log.getStaffId() + " | Action: " + log.getAction() + " | Time: " + log.getTimestamp());
            }
        } catch (Exception e) {
            logModel.addElement("Error loading audit logs: " + e.getMessage());
        }
    }
}
