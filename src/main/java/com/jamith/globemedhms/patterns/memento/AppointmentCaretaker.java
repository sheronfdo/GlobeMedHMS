package com.jamith.globemedhms.patterns.memento;

import java.util.Stack;

public class AppointmentCaretaker {
    private final Stack<AppointmentMemento> mementos = new Stack<>();

    public void save(AppointmentMemento memento) {
        mementos.push(memento);
    }

    public AppointmentMemento undo() {
        if (!mementos.isEmpty()) {
            return mementos.pop();
        }
        return null;
    }
}