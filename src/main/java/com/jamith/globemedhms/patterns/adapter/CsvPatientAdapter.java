package com.jamith.globemedhms.patterns.adapter;

import com.jamith.globemedhms.core.entities.Patient;

public class CsvPatientAdapter {
    public Patient adaptFromCsv(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length >= 5) {
            return new Patient(data[0], data[1], data[2], data[3], data[4]);
        }
        return null;
    }

}