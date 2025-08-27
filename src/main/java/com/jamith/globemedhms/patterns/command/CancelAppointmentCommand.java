package com.jamith.globemedhms.patterns.command;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;

public class CancelAppointmentCommand implements AppointmentCommand {
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private final Appointment appointment;
    private final String originalStatus;

    public CancelAppointmentCommand(Appointment appointment) {
        this.appointment = appointment;
        this.originalStatus = appointment.getStatus();
    }

    @Override
    public void execute() {
        appointment.setStatus("CANCELLED");
        appointmentService.saveOrUpdateAppointment(appointment);
    }

    @Override
    public void undo() {
        appointment.setStatus(originalStatus);
        appointmentService.saveOrUpdateAppointment(appointment);
    }
}