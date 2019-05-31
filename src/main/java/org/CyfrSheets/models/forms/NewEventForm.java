package org.CyfrSheets.models.forms;

import org.CyfrSheets.models.Participant;
import org.CyfrSheets.models.SEvent;
import org.CyfrSheets.models.StaticEvent;
import org.CyfrSheets.models.exceptions.EventTypeMismatchException;
import org.CyfrSheets.models.exceptions.InsufficientEventDataException;
import org.CyfrSheets.models.exceptions.InvalidDateTimeArrayException;
import org.CyfrSheets.models.utilities.EventBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DATE;

public class NewEventForm {

    private SEvent event = null;

    private String eventName =  null;
    private String eventDesc = null;

    private Boolean statEvent = null;
    private Boolean oneTime = null;

    private Calendar.Builder cB;

    // Checker booleans for use of cB across methods
    private boolean sTB = false;
    private boolean sDB = false;
    private boolean eTB = false;
    private boolean eDB = false;

    private Calendar startTime = null;
    private Calendar endTime = null;

    // Create and hand in to webpage - will be populated by the inputs and handed off to event construction methods
    public NewEventForm() {
        cB = new Calendar.Builder();
        cB.setCalendarType("gregorian");
    }

    // Build event utilizing the EventBuilder and data accrued and accumulated here. ... I suppose this is mostly the
    // builder itself but this makes it easier to work with in this context. I think.
    public SEvent buildEvent(Participant p) throws Exception {
        if (event != null) return event;

        if (startTime == null) throw new InsufficientEventDataException("Event has no start time");

        if (statEvent == null) throw new InsufficientEventDataException("Event not tagged as Static or Planning");

        if (oneTime == null && statEvent) throw new InsufficientEventDataException("Static event not tagged as Start-Time-Only or Time Slot");

        if ((!statEvent || !oneTime) && endTime == null) throw new InsufficientEventDataException("Event tagged as span lacks end time");

        EventBuilder eb = new EventBuilder();

        // Static event block
        if (statEvent) {
            try {
                if (oneTime) return eb.statInitStartOnly(eventName, eventDesc, startTime, p);
                if (startTime.get(DATE) == endTime.get(DATE)) return eb.statInitTimeRange(eventName, eventDesc,
                        startTime, endTime, p);
            } catch (EventTypeMismatchException e) {
                String eMsg = "EventTypeMismatchException in buildEvent > static events. Error message follows: " +
                        e.getMessage();
                throw new EventTypeMismatchException(eMsg);
            }
        }

        // Planning event block
        // TODO - implement this block. Leave the above comment. Because I know you won't, future me. Sincerely, past you.

        // remove when complete
        return new SEvent();
    }

    // Wipes the form
    public void clearForm() {

        event = null;

        eventName =  null;
        eventDesc = null;

        statEvent = null;
        oneTime = null;

        sTB = false;
        sDB = false;
        eTB = false;
        eDB = false;

        startTime = null;
        endTime = null;
    }

    // Clear form, but as a static.
    public static void clearForm(NewEventForm form) { form.clearForm(); }

    // Populate All Fields
    public void populateForm(String eventName, String eventDesc, boolean statEvent, boolean oneTime, String startDate, String startTime, String endDate, String endTime) throws InvalidDateTimeArrayException {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.statEvent = statEvent;
        this.oneTime = oneTime;

       setStartDate(startDate);
       setStartTime(startTime);

        if (oneTime) {
            this.endTime = null;
        } else {
            setEndDate(endDate);
            setEndTime(endTime);
        }
    }

    public SEvent getEvent() { return event; }

    public String getName() { return eventName; }
    public String getDesc() { return eventDesc; }

    public void setName(String name) { this.eventName = name; }
    public void setDesc(String desc) { this.eventDesc = desc; }

    public boolean isStatic() { return statEvent; }
    public boolean startOnly() { return oneTime; }

    public void setStaticEvent() { statEvent = true; }
    public void setPlanningEvent() {
        statEvent = false;
        oneTime = false;
    }

    public void setStartOnly() { oneTime = true; }
    public void setDateRange() { oneTime = false; }

    public void setStartDate (String date) throws InvalidDateTimeArrayException {

        int[] dA = parseDate(date);
        cB.setDate(dA[0],dA[1],dA[2]);

        sDB = true;
        eDB = false;

        if (sTB) this.startTime = cB.build();
    }

    public void setStartTime (String time) throws InvalidDateTimeArrayException {

        int[] tA = parseTime(time);
        cB.setTimeOfDay(tA[0],tA[1], 0);

        sTB = true;
        eTB = false;

        if (sDB) this.startTime = cB.build();
    }

    public void setEndDate (String date) throws InvalidDateTimeArrayException {

        int[] dA = parseDate(date);
        cB.setDate(dA[0],dA[1],dA[2]);

        eDB = true;
        sDB = false;

        if (eTB) {
            this.endTime = cB.build();
            oneTime = false;
        }
    }

    public void setEndTime (String time) throws InvalidDateTimeArrayException {

        int[] tA = parseTime(time);
        cB.setTimeOfDay(tA[0],tA[1], 0);

        eTB = true;
        sTB = false;

        if (eDB) {
            this.endTime = cB.build();
            oneTime = false;
        }
    }

    private int[] parseDate(String dateStr) throws InvalidDateTimeArrayException {
        ArrayList<Integer> parsed = parseInts(dateStr);

        int[] dA = new int[3];

        if (parsed.size() == 3) for (int i : parsed) dA[parsed.indexOf(i)] = i;
        else if (parsed.size() > 3) {
            String eStr = "Date String/Array has too many integers: ";
            for (int i : parsed) eStr += i + " ";
            throw new InvalidDateTimeArrayException(eStr); }
        else {
            String eStr = "Date String/Array does not have enough integers: ";
            if (parsed.size() <= 0) eStr += "NO INTEGERS FOUND" + dateStr;
            else for (int i: parsed) eStr += i + " ";
            throw new InvalidDateTimeArrayException(eStr); }

        dA[1] -= 1;

        return dA;
    }

    private int[] parseTime(String timeStr) throws InvalidDateTimeArrayException {
        ArrayList<Integer> parsed = parseInts(timeStr);

        int[] tA = new int[2];

        if (parsed.size() == 2) for (int i : parsed) tA[parsed.indexOf(i)] = i;
        else if (parsed.size() > 2) {
            String eStr = "Time String/Array has too many integers: ";
            for (int i : parsed) eStr += i + " ";
            throw new InvalidDateTimeArrayException(eStr); }
        else {
            String eStr = "Time String/Array does not have enough integers: ";
            if (parsed.size() <= 0) eStr += "NO INTEGERS FOUND" + timeStr;
            else for (int i: parsed) eStr += i + " "; }

        return tA;
    }

    private ArrayList<Integer> parseInts(String parseThis) {
        char[] cArray = parseThis.toCharArray();
        ArrayList<Integer> output = new ArrayList<>();
        boolean lastInt = false;
        int buffer = 0;

        for (Character c : cArray) {
            if (Character.isDigit(c)) {
                if (lastInt) buffer *= 10;
                buffer += Character.getNumericValue(c);
                lastInt = true;
                continue;
            }
            output.add(buffer);
            lastInt = false;
            buffer = 0;
        }

        return output;
    }

}