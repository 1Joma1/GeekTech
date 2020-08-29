package com.joma.geektech.model;

import java.util.List;

public class Event {
    List<String> events;

    public Event() {
    }

    public Event(List<String> events) {
        this.events = events;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Event{" +
                "events=" + events +
                '}';
    }
}
