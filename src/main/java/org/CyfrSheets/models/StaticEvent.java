package org.CyfrSheets.models;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Calendar;

@Entity
public class StaticEvent extends SEvent {

    private EventType type;
    private EventTime time;

    private ArrayList<Participant> participants = new ArrayList<>();

    private String desc;

    public StaticEvent(EventType type, EventTime time,String name, String desc, Participant creator) { super(type, time, name, desc, creator); }

    public StaticEvent() { }

    public StaticEvent initStartOnly(EventType type, String name, String desc, Calendar timeCal) {
        EventTime time = new EventTime(this,true);



        return new StaticEvent();
    }
}
