package org.CyfrSheets.models;

import org.CyfrSheets.models.utilities.EnuMonth;

import java.util.Calendar;

public class EventTime {

    private TimeType tType;

    private SEvent parent;

    private EventType parentType;

    private boolean staticEvent;
    private boolean startOnly;

    private Calendar startTime;
    private Calendar endTime;

    private EnuMonth startMonth;
    private EnuMonth endMonth;

    private int startDay;
    private int endDay;

    private int startHour;
    private int endHour;

    private int startMinute;
    private int endMinute;

    public EventTime(SEvent parent, boolean startOnly) {
        parentType = parent.getType();
        this.startOnly = startOnly;
        if ( parentType.isPlanning() ) {
            planningInit();
        } else {
            staticInit();
        }
    }

    public EventTime() { }

    public EnuMonth getStartMonth() { return startMonth; }

    private void staticInit() {
        staticEvent = true;
        if (startOnly) {
            endMonth = EnuMonth.NULLMONTH;
            endDay = -1;
            endHour = -1;
            endMinute = -1;
        }
    }

    private void planningInit() {
        staticEvent = false;
        startOnly = false;
    }

    public String toString() {
        String output = new String();

        if (!staticEvent) {
            output = "Somewhere from ";
        } else if(!startOnly) {
            output = "From";
        }

        output += startMonth.getName() + " " + startDay;
        output = numSuffix(startDay, output);
        output += "at ";

        switch (tType) {
            case TWELVE:
                boolean am = true;
                if (startHour > 12) {
                    output += (startHour - 12);
                    am = false;
                }
                else if (startHour == 0) output += "12";
                else output += startHour;
                if (startHour == 12) am = false;
                output += ":" + startMinute;
                if (am) output += "AM";
                else output += "PM";
            case TWENTYFOUR:
                if (startHour < 10) output += "0";
                output += startHour + startMinute + "Hours";
        }

        if (startOnly) return output;

        output += " to ";


        switch (tType) {
            case TWELVE:
                boolean am = true;
                if (endHour > 12) {
                    output += (endHour - 12);
                    am = false;
                }
                else if (endHour == 0) output += "12";
                else output += endHour;
                if (endHour == 12) am = false;
                output += ":" + endMinute;
                if (am) output += "AM";
                else output += "PM";
            case TWENTYFOUR:
                if (endHour < 10) output += "0";
                output += endHour + endMinute + "Hours";
        }

        if (startDay == endDay) return output;

        output += " on the ";
        output = numSuffix(endDay, output);

        if (startMonth == endMonth) return output;

        output += " of " + endMonth;

        return output;
    }

    private String numSuffix(int num, String input) {
        String sInput = input;
        input = isFirst(num, input);
        input = isSecond(num, input);
        input = isThird(num, input);
        if (sInput.equals(input)) {
            input += "th, ";
        }
        return input;
    }

    private String isFirst(int num, String input) {
        if (num == 1 || num == 21 || num == 31) input += "st, ";
        return input;
    }
    private String isSecond(int num, String input) {
        if (num == 2 || num == 22) input += "nd, ";
        return input;
    }
    private String isThird(int num, String input) {
        if (num == 3 || num == 23) input += "rd, ";
        return input;
    }

}

enum TimeType { TWELVE, TWENTYFOUR }