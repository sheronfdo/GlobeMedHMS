package com.jamith.globemedhms.patterns.command;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;

public class BookAppointmentCommand implements AppointmentCommand {
    private final AppointmentService appointmentService = new AppointmentServiceImpl();
    private final Appointment appointment;

    public BookAppointmentCommand(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public void execute() {
        appointmentService.saveOrUpdateAppointment(appointment);
    }

    @Override
    public void undo() {
        // Placeholder for undo (e.g., cancel appointment)
        appointment.setStatus("CANCELLED");
        appointmentService.saveOrUpdateAppointment(appointment);
    }
}