package org.CyfrSheets.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

public class Event {

    //TODO: Implement Event Constructor - Needs date/time (Use 'Calendar'?), Name, Description, Key/ID(?)

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=64)
    private String name;

    @NotNull
    @Size(min=1, max=4096, message="Description may not exceed 4096 characters in length")
    private String description;

    // Date data
    @NotNull
    private Month month;
    private byte day;
    private short year;
    private byte hour;
    private byte minute;

    private List<Participant> participants;

    public Event(String name, String description, Month month, byte day, short year, byte hour, byte minute) {
        this.name = name;
        if(description == null || description.isEmpty()) {
            this.description = "[No Description]";
        } else {
            this.description = description;
        }
    }

    public Event() { }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public HashMap<String,String> getDateTimeStringMap() {

        HashMap<String,String> dtd = new HashMap<>(); // date-time-data abbreviated

        // Figure out how to convert this reasonably. Later.

        return dtd;

    }


}
