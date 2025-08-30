package com.jamith.globemedhms.patterns.memento;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Patient;
import com.jamith.globemedhms.core.entities.Staff;

public class AppointmentMemento {
    private final int id;
    private final Staff staff;
    private final Patient patient;
    private final String date;
    private final String time;
    private final String type;
    private final String status;
    private final String prescription;
    private final String treatmentDetails;

    public AppointmentMemento(Appointment appointment) {
        this.id = appointment.getId();
        this.staff = appointment.getStaff();
        this.patient = appointment.getPatient();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.type = appointment.getType();
        this.status = appointment.getStatus();
        this.prescription = appointment.getPrescription();
        this.treatmentDetails = appointment.getTreatmentDetails();
    }

    public Appointment restore() {
        Appointment appointment = new Appointment(staff, patient, date, time, type, status);
        appointment.setId(id);
        appointment.setPrescription(prescription);
        appointment.setTreatmentDetails(treatmentDetails);
        return appointment;
    }
}