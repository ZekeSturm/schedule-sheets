package org.CyfrSheets.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Entity
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

    /**
    // (old now) Date data
    @NotNull
    private Month month;
    private byte day;
    private short year;
    private byte hour;
    private byte minute;
    */

    @NotNull
    private Calendar startTime;

    @NotNull
    private Calendar endTime;

    @ManyToMany(mappedBy = "events")
    private List<Participant> participants;

    public Event(String name, String description, Calendar startTime, Calendar endTime) {
        this.name = name;
        if(description == null || description.isEmpty()) {
            this.description = "[No Description]";
        } else {
            this.description = description;
        }
        this.startTime = startTime;
        this.endTime = endTime;
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
    /**
        public Month getMonth() { return month; }
        public String getMonthString() { return month.getDisplayName(TextStyle.FULL,Locale.ENGLISH); }
        public byte getDay() { return day; }
        public short getYear() { return year; }
        public byte getHour() { return hour; }
        public byte getMinute() { return minute; }
     */
    public Calendar getStartTime() { return startTime; }
    public String getStartString() {
        // TODO: Implement this
        return "incomplete feature";
    }
    public Calendar getEndTime() { return endTime; }
    public String getEndString() {
        // TODO: Implement this.
        return "incomplete feature";
    }
    public List<Participant> getParticipants() { return participants; }

    public Participant getUnregParticipant(String name) {
        for (Participant p : participants) {
            if (p.checkName(name)) { return p; }
        }
        return null;
    }

    public User getRegUser(int userID) {
        for (Participant p : participants) {
            if (!p.isUser()) { continue; }
            User u = (User)p;
            if (u.checkID(userID)) { return u; }
        }
        return null;
    }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    /**
        public void setMonth(Month month) { this.month = month; }
        public void setDay(byte day) { this.day = day; }
        public void setYear(short year) { this.year = year; }
        public void setHour(byte hour) { this.hour = hour; }
        public void setMinute(byte minute) { this.minute = minute; }
     */
    public void setStartTime(Calendar startTime) { this.startTime = startTime; }
    public void setEndTime(Calendar endTime) {this.endTime = endTime; }
}
