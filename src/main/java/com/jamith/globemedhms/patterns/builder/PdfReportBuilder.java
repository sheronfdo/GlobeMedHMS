package com.jamith.globemedhms.patterns.builder;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PdfReportBuilder {
    private String title;
    private String entityType;
    private Object entity;
    private List<ReportField> fields;
    
    public PdfReportBuilder() {
        this.fields = new ArrayList<>();
    }
    
    public PdfReportBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public PdfReportBuilder setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    
    public PdfReportBuilder setEntity(Object entity) {
        this.entity = entity;
        return this;
    }
    
    public PdfReportBuilder addField(String fieldName, String fieldValue) {
        this.fields.add(new ReportField(fieldName, fieldValue));
        return this;
    }
    
    public String build() {
        if (title == null || entity == null) {
            throw new IllegalStateException("Title and entity must be set");
        }
        
        String filename = generateFilename();
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);
            
            // Add entity info
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
            Paragraph entityPara = new Paragraph("Entity: " + entityType, headerFont);
            entityPara.setSpacingAfter(10);
            document.add(entityPara);
            
            // Add fields table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);
            
            Font fieldHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            Font fieldValueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
            
            for (ReportField field : fields) {
                PdfPCell headerCell = new PdfPCell(new Phrase(field.getName(), fieldHeaderFont));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setPadding(5);
                headerCell.setBorderWidth(1);
                
                PdfPCell valueCell = new PdfPCell(new Phrase(field.getValue(), fieldValueFont));
                valueCell.setPadding(5);
                valueCell.setBorderWidth(1);
                
                table.addCell(headerCell);
                table.addCell(valueCell);
            }
            
            document.add(table);
            
            // Add footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Generated on: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);
            
            document.close();
            return filename;
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report: " + e.getMessage(), e);
        }
    }
    
    private String generateFilename() {
        String baseName = title.toLowerCase().replace(" ", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseName + "_" + timestamp + ".pdf";
    }
    
    private static class ReportField {
        private String name;
        private String value;
        
        public ReportField(String name, String value) {
            this.name = name;
            this.value = value != null ? value : "N/A";
        }
        
        public String getName() { return name; }
        public String getValue() { return value; }
    }
}