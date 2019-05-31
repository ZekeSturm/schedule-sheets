package org.CyfrSheets.models.forms;

import org.CyfrSheets.models.Participant;
import org.CyfrSheets.models.SEvent;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.ArrayList;
import java.util.Calendar;

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

    public SEvent buildEvent(Participant p) throws InsufficientEventDataException {
        if (event != null) return event;

        if (startTime == null) throw new InsufficientEventDataException("Event has no start time");

        if (statEvent == null) throw new InsufficientEventDataException("Event not tagged as Static or Planning");

        if (oneTime == null && statEvent) throw new InsufficientEventDataException("Static event not tagged as Start-Time-Only or Time Slot");

        if ((!statEvent || !oneTime) && endTime == null) throw new InsufficientEventDataException("Event tagged as span lacks end time");



        if (statEvent) {

        }

        // remove when complete
        return new SEvent();
    }

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
        ArrayList<Integer> dateList = parseInts(date);

        if (dateList.size() >= 3) {
            int[] dA = new int[3];
            for (int i = 0; i < 3 ; i++) dA[i] = dateList.get(i);
            cB.setDate(dA[0],dA[1],dA[2]);
        } else throw new InvalidDateTimeArrayException("Date String/Array does not have enough integers");

        sDB = true;
        eDB = false;

        if (sTB) this.startTime = cB.build();
    }

    public void setStartTime (String time) throws InvalidDateTimeArrayException {
        ArrayList<Integer> timeList = parseInts(time);

        if (timeList.size() >= 2) {
            int[] tA = new int[2];
            for (int i = 0; i < 2; i++) tA[i] = timeList.get(i);
            cB.setTimeOfDay(tA[0],tA[1],0);
        } else throw new InvalidDateTimeArrayException("Time String/Array does not have enough integers");

        sTB = true;
        eTB = false;

        if (sDB) this.startTime = cB.build();
    }

    public void setEndDate (String date) throws InvalidDateTimeArrayException {
        ArrayList<Integer> dateList = parseInts(date);

        if (dateList.size() >= 3) {
            int[] dA = new int[3];
            for (int i = 0; i < 3 ; i++) dA[i] = dateList.get(i);
            cB.setDate(dA[0],dA[1],dA[2]);
        } else throw new InvalidDateTimeArrayException("Date String/Array does not have enough integers");

        eDB = true;
        sDB = false;

        if (eTB) {
            this.endTime = cB.build();
            oneTime = false;
        }
    }

    public void setEndTime (String time) throws InvalidDateTimeArrayException {
        ArrayList<Integer> timeList = parseInts(time);

        if (timeList.size() >= 2) {
            int[] tA = new int[2];
            for (int i = 0; i < 2; i++) tA[i] = timeList.get(i);
            cB.setTimeOfDay(tA[0], tA[1], 0);
        } else throw new InvalidDateTimeArrayException("Time String/Array does not have enough integers");

        eTB = true;
        sTB = false;

        if (eDB) {
            this.endTime = cB.build();
            oneTime = false;
        }
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

// Handle strings that do not produce valid data for insertion into Calendar objects
class InvalidDateTimeArrayException extends Exception {
    public InvalidDateTimeArrayException(String s) { super(s); }
}

// Handle missing data fields in event builder
class InsufficientEventDataException extends Exception {
    public InsufficientEventDataException(String s) { super(s); }
}
