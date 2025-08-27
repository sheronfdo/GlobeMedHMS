package com.jamith.globemedhms.application.services.appointment;

import com.jamith.globemedhms.core.entities.Appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByStaff(int staffId);
    void saveOrUpdateAppointment(Appointment appointment);
    boolean hasConflict(int staffId, String date, String time);
    void completeAppointment(Appointment appointment, String treatmentDetails, String prescription);
}