package com.jamith.globemedhms.patterns.composite;

public class ReportSection implements ReportComponent {
    private String title;
    private String content;

    public ReportSection(String title) {
        this.title = title;
        this.content = "";
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String generate() {
        return "=== " + title + " ===\n" + content + "\n\n";
    }

    @Override
    public void add(ReportComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf section");
    }

    @Override
    public void remove(ReportComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf section");
    }

    @Override
    public ReportComponent getChild(int index) {
        throw new UnsupportedOperationException("Leaf section has no children");
    }
}