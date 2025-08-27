package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.command.BookAppointmentCommand;
import com.jamith.globemedhms.patterns.command.CancelAppointmentCommand;
import com.jamith.globemedhms.presentation.views.appointment.AppointmentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentController {
    private final AppointmentView view;
    private final AppointmentService service = new AppointmentServiceImpl();
    private final Staff loggedInStaff;

    public AppointmentController(AppointmentView view, Staff loggedInStaff) {
        this.view = view;
        this.loggedInStaff = loggedInStaff;
        view.addBookListener(new BookListener());
        view.addCancelListener(new CancelListener());
        view.addCompleteListener(new CompleteListener());
    }

    class BookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Appointment appointment = new Appointment(view.getSelectedStaff(), view.getSelectedPatient(), view.getDate(), view.getTime(), view.getType(), "SCHEDULED");
            BookAppointmentCommand command = new BookAppointmentCommand(appointment);
            command.execute();
            view.updateAppointmentList(loggedInStaff);
            view.showMessage("Appointment booked successfully!");
        }
    }

    class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Appointment selectedAppointment = view.getSelectedAppointment();
            if (selectedAppointment != null) {
                CancelAppointmentCommand command = new CancelAppointmentCommand(selectedAppointment);
                command.execute();
                view.updateAppointmentList(loggedInStaff);
                view.showMessage("Appointment cancelled successfully!");
            } else {
                view.showMessage("Please select an appointment.");
            }
        }
    }

    class CompleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Appointment selectedAppointment = view.getSelectedAppointment();
            if (selectedAppointment != null) {
                String treatmentDetails = view.getTreatmentDetails();
                String prescription = view.getPrescription();
                service.completeAppointment(selectedAppointment, treatmentDetails, prescription);
                view.updateAppointmentList(loggedInStaff);
                view.showMessage("Appointment completed successfully!");
            } else {
                view.showMessage("Please select an appointment.");
            }
        }
    }
}