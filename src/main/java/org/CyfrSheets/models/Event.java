package org.CyfrSheets.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Event {

    //TODO: Implement Event Constructor - Needs date/time (Use 'Calendar'?), Name, Description, Key/ID

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

    public void addParticipant(Participant participant) {
        if ( participants.contains(participant) ) { return; }
        participants.add(participant);
    }

    public boolean removeParticipant(Participant participant) {
        if ( participants.contains(participant) ) {
            participants.remove(participant);
            return true;
        } else {
            return false;
        }
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Month getMonth() { return month; }
    public String getMonthString() { return month.getDisplayName(TextStyle.FULL,Locale.ENGLISH); }
    public byte getDay() { return day; }
    public short getYear() { return year; }
    public byte getHour() { return hour; }
    public byte getMinute() { return minute; }
    public List<Participant> getParticipants() { return participants; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setMonth(Month month) { this.month = month; }
    public void setDay(byte day) { this.day = day; }
    public void setYear(short year) { this.year = year; }
    public void setHour(byte hour) { this.hour = hour; }
    public void setMinute(byte minute) { this.minute = minute; }

}
