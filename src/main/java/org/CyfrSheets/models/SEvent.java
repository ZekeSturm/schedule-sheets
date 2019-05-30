package org.CyfrSheets.models;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class SEvent {

    private EventType type;
    private EventTime time;

    private ArrayList<Participant> participants = new ArrayList<>();

    private String desc;

    public SEvent(EventType type, EventTime time, String desc) {
        this.type = type;
        this.time = time;
        this.desc = desc;
    }

    public SEvent() { }

    public void addParticipant(Participant person) {
        if ( person.isUser() ) {
            participants.add(person);
        } else {

        }
    }

    public EventType getType() { return type; }
    public EventTime getTime() { return time; }
    public String getDesc() { return desc; }
    public ArrayList<Participant> getParticipants() { return participants; }
}
