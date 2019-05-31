package org.CyfrSheets.models;

import org.CyfrSheets.models.exceptions.EventTypeMismatchException;

import java.util.Calendar;

import static org.CyfrSheets.models.EventType.*;

public class EventTime {

    private TimeType tType;

    private EventType parentType;

    private boolean staticEvent;
    private boolean startOnly;

    private Calendar startTime;
    private Calendar endTime;

    // Implement Planning Event participant/time pairs in hashmaps here... at some point.

    public EventTime(EventType parentType, Calendar startTime, Calendar endTime)
            throws EventTypeMismatchException {
        this.parentType = parentType;

        if (parentType == SDP || parentType == MDP) {
            if (startOnly) throw new EventTypeMismatchException("Planning Event Must Have End Time");
            planningInit(startTime, endTime);
        } else {
            if (startOnly) staticInit(startTime);
            else staticInit(startTime, endTime);
        }
    }

    public EventTime(Calendar startTime) throws EventTypeMismatchException
    { this(SOS,startTime,null); }

    public EventTime() { }

    public Calendar getStartTime() { return startTime; }
    public Calendar getEndTime() throws EventTypeMismatchException {
        if (startOnly) throw new EventTypeMismatchException("Events tagged with startOnly do not have an end time");
        return endTime;
    }

    public EventType fetchType() { return parentType; }

    // When adding below getters/setters, add checks for "is this a planning event or not". Throw
    // EventTypeMismatchException for attempting to access from static event

    // Getter methods for participant/time pairs go here. May make these a further subclass but it would be an in-class
    // subclass and would still export standard hashmap key/value pairs at the public level

    // Setter methods for participant/time pairs go here. See above. Required for planning events.

    private void staticInit(Calendar startTime, Calendar endTime) {
        this.endTime = endTime;
        staticInit(startTime);
    }

    private void staticInit(Calendar startTime) {
        this.startTime = startTime;
        staticEvent = true;
    }

    private void planningInit(Calendar startTime, Calendar endTime) {
        staticEvent = false;
    }
}

enum TimeType { TWELVE, TWENTYFOUR }

/**
 Legacy typefinder code - may be useful to export elsewhere

    private EventType findParentType(Calendar startTime, Calendar endTime) {
        if (endTime == null) return SOS;
        if (startTime.get(DATE) == endTime.get(DATE)) {
            if (staticEvent) return SDS;
            else return SDP;
        } else {
            if (staticEvent) return MDS;
            else return MDP;
        }
    }
 */