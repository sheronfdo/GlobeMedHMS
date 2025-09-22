package com.jamith.globemedhms.patterns.composite;

public interface ReportComponent {
    String generate();
    void add(ReportComponent component);
    void remove(ReportComponent component);
    ReportComponent getChild(int index);
}