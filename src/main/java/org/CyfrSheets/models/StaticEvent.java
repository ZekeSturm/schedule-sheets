package org.CyfrSheets.models;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StaticEvent extends SEvent {

    public StaticEvent(EventType type, EventTime time, String name, String desc, Participant creator)
    { super(type, time, name, desc, creator); }

    public StaticEvent() { }

    protected boolean creatorCheck(Participant p)
    { return super.creatorCheck(p); }

    public int getId() { return super.getId(); }
    public String getName() { return super.getName(); }
    public String getDesc() { return super.getDesc(); }
    public EventType getType() { return super.getType(); }
    public EventTime getTime() { return super.getTime(); }
    public ArrayList<Participant> getParticipants() { return super.getParticipants(); }
    public ArrayList<TempUser> getTempUsers() { return super.getTempUsers(); }
    public List<User> getRegUsers() { return super.getRegUsers(); }
}
