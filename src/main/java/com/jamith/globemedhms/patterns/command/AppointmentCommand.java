package com.jamith.globemedhms.patterns.command;

import com.jamith.globemedhms.core.entities.Appointment;

public interface AppointmentCommand {
    void execute();
    void undo();
}