package com.jamith.globemedhms.presentation.controllers;

import com.jamith.globemedhms.application.services.appointment.AppointmentService;
import com.jamith.globemedhms.application.services.appointment.AppointmentServiceImpl;
import com.jamith.globemedhms.core.entities.Appointment;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.patterns.memento.AppointmentCaretaker;
import com.jamith.globemedhms.patterns.memento.AppointmentMemento;
import com.jamith.globemedhms.patterns.proxy.ResourceProxy;
import com.jamith.globemedhms.presentation.views.appointment.AppointmentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentController {
    private final AppointmentView view;
    private final AppointmentService service = new AppointmentServiceImpl();
    private final ResourceProxy proxy = new ResourceProxy();
    private final Staff loggedInStaff;
    private final AppointmentCaretaker caretaker = new AppointmentCaretaker();

    public AppointmentController(AppointmentView view, Staff loggedInStaff) {
        this.view = view;
        this.loggedInStaff = loggedInStaff;
        view.addBookListener(new BookListener());
        view.addCancelListener(new CancelListener());
        view.addCompleteListener(new CompleteListener());
        view.addUndoListener(e -> undo());
    }

    class BookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(loggedInStaff, "APPOINTMENT", "UPDATE_PATIENT_RECORDS");
                Appointment appointment = new Appointment(view.getSelectedStaff(), view.getSelectedPatient(),
                        view.getDate(), view.getTime(), view.getType(), "SCHEDULED");
                caretaker.save(new AppointmentMemento(appointment)); // Save state
                service.saveOrUpdateAppointment(appointment);
                view.updateAppointmentList(loggedInStaff);
                view.showMessage("Appointment booked successfully!");
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            } catch (IllegalStateException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }

    class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(loggedInStaff, "APPOINTMENT", "UPDATE_PATIENT_RECORDS");
                Appointment selectedAppointment = view.getSelectedAppointment();
                if (selectedAppointment != null) {
                    caretaker.save(new AppointmentMemento(selectedAppointment)); // Save state
                    selectedAppointment.setStatus("CANCELLED");
                    service.saveOrUpdateAppointment(selectedAppointment);
                    view.updateAppointmentList(loggedInStaff);
                    view.showMessage("Appointment cancelled successfully!");
                } else {
                    view.showMessage("Please select an appointment.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }

    class CompleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                proxy.accessResource(loggedInStaff, "PRESCRIPTION", "PRESCRIBE_MEDICATIONS");
                Appointment selectedAppointment = view.getSelectedAppointment();
                System.out.println("selectedAppointment : " + selectedAppointment);
                if (selectedAppointment != null) {
                    caretaker.save(new AppointmentMemento(selectedAppointment)); // Save state
                    String treatmentDetails = view.getTreatmentDetails();
                    String prescription = view.getPrescription();
                    System.out.println("prescription : " + prescription);
                    System.out.println("treatment details : " + treatmentDetails);
                    if (prescription == null || prescription.trim().isEmpty()) {
                        view.showMessage("Prescription cannot be empty for completion.");
                        return;
                    }
                    service.completeAppointment(selectedAppointment, treatmentDetails, prescription);
                    view.updateAppointmentList(loggedInStaff);
                    view.showMessage("Appointment completed successfully with prescription!");
                } else {
                    view.showMessage("Please select an appointment.");
                }
            } catch (SecurityException ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }

    public void undo() {
        AppointmentMemento memento = caretaker.undo();
        if (memento != null) {
            Appointment restored = memento.restore();
            service.saveOrUpdateAppointment(restored);
            view.updateAppointmentList(loggedInStaff);
            view.showMessage("Operation undone successfully!");
        } else {
            view.showMessage("No operation to undo.");
        }
    }
}