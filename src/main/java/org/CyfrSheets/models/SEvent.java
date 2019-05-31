package org.CyfrSheets.models;

import org.CyfrSheets.models.exceptions.UnregisteredUserException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SEvent {

    // Class name short for Scheduled Event

    @Id
    @GeneratedValue
    private int id;

    private Participant creator;
    private ArrayList<Participant> ops;

    private EventType type;
    private EventTime time;

    private ArrayList<Participant> participants = new ArrayList<>();

    private ArrayList<TempUser> tempUsers = new ArrayList<>();

    @ManyToMany
    private List<User> regUsers;

    private String name;

    private String desc;

    // Possible location field goes here

    public SEvent(EventType type, EventTime time, String name, String desc, Participant creator) {
        this.name = name;
        this.type = type;
        this.time = time;
        this.desc = desc;
        this.creator = creator;
    }

    public SEvent() { }

    public void addParticipant(Participant person) {
        if ( person.isUser() ) {
            User userPerson = (User) person;
            participants.add(userPerson);
            regUsers.add(userPerson);
        } else {
            TempUser tempPerson = (TempUser) person;
            participants.add(tempPerson);
            tempUsers.add(tempPerson);
        }
    }

    // TODO: implement name and description changes. Reliant on ability to maintain session/pass participants along. Noncritical

    /**
    public boolean changeName(String name)  {
        Participant test = new Participant();
        try {
            userCheck(test);

            return true;
        } catch(UnregisteredUserException e){

            return false;
        }
    }

     public boolean changeDesc(String desc) {
        // Implement
        return false;
     }
     */

    // Essentially unnecessary until the above is implemented
    protected boolean creatorCheck(Participant p) throws UnregisteredUserException {
        if (p.isUser()) {
            if (!p.isEqual(creator)) {
                return false;
            }
            return true;
        }
        else throw new UnregisteredUserException("You are not authorized to make this change");
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDesc() { return desc; }
    public EventType getType() { return type; }
    public EventTime getTime() { return time; }
    public ArrayList<Participant> getParticipants() { return participants; }
    public ArrayList<TempUser> getTempUsers() { return tempUsers; }
    public List<User> getRegUsers() { return regUsers; }
}
