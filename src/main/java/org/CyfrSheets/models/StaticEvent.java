package org.CyfrSheets.models;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class StaticEvent extends SEvent {

    private EventType type;
    private EventTime time;

    private ArrayList<Participant> participants = new ArrayList<>();

    private String desc;

    public StaticEvent(EventType type, EventTime time, String desc) { super(type, time, desc); }

    public StaticEvent() { }
}
