package com.jamith.globemedhms.patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class ReportComposite implements ReportComponent {
    private String title;
    private List<ReportComponent> sections;

    public ReportComposite(String title) {
        this.title = title;
        this.sections = new ArrayList<>();
    }

    public void addSection(ReportComponent section) {
        sections.add(section);
    }

    @Override
    public String generate() {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════════════\n");
        report.append("                 ").append(title).append("\n");
        report.append("═══════════════════════════════════════════════════\n\n");
        
        for (ReportComponent section : sections) {
            report.append(section.generate());
        }
        
        report.append("═══════════════════════════════════════════════════\n");
        report.append("End of Report\n");
        report.append("═══════════════════════════════════════════════════\n");
        
        return report.toString();
    }

    @Override
    public void add(ReportComponent component) {
        sections.add(component);
    }

    @Override
    public void remove(ReportComponent component) {
        sections.remove(component);
    }

    @Override
    public ReportComponent getChild(int index) {
        return sections.get(index);
    }

    public int getSectionCount() {
        return sections.size();
    }
}