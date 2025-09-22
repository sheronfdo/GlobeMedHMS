package com.jamith.globemedhms.application.services.appointment;

import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Staff;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByStaff(Staff staff);
    void saveOrUpdateAppointment(Appointment appointment);
    boolean hasConflict(int staffId, String date, String time);
    void completeAppointment(Appointment appointment, String treatmentDetails, String prescription);
    List<Object[]> getAppointmentStatusCount();
    List<Object[]> getAppointmentTypeCount();
    List<Object[]> getMonthlyAppointmentTrend();
    List<Object[]> getStaffAppointmentCount();
    long getTotalAppointmentCount();
    long getCompletedAppointmentCount();
}