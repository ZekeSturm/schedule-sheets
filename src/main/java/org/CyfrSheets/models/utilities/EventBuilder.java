package org.CyfrSheets.models.utilities;

import org.CyfrSheets.models.*;
import org.CyfrSheets.models.exceptions.EventTypeMismatchException;

import java.util.Calendar;

import static java.util.Calendar.*;
import static org.CyfrSheets.models.EventType.*;

public class EventBuilder {

    // Initialize Static event with the start only flag
    public StaticEvent statInitStartOnly (String name, String desc, Calendar timeCal, Participant creator)
            throws EventTypeMismatchException {
        try {
            EventTime time = new EventTime(timeCal);
            return new StaticEvent(SOS, time, name, desc, creator);
        } catch (EventTypeMismatchException e) {
            throw new EventTypeMismatchException
                    ("Somehow, you've managed to force this to make an error (StaticEvent.initStartOnly). " +
                            "The error message was as follows:\n" + e.getMessage());
        }
    }

    // Initialize Static event without start only flag/with time range flag.
    // ... They're the same boolean. I didn't know how to phrase it elegantly
    public StaticEvent statInitTimeRange (String name, String desc, Calendar startTime, Calendar endTime,
                                          Participant creator) throws EventTypeMismatchException {
        EventType type;
        try {
            if (startTime.get(DATE) == endTime.get(DATE)) type = SDS;
            else type = MDS;
            EventTime time = new EventTime(type, startTime, endTime);
            return new StaticEvent(type, time, name, desc, creator);
        } catch (EventTypeMismatchException e) {
            throw new EventTypeMismatchException
                    ("Somehow, you've managed to force this to make an error (StaticEvent.initDateRange). " +
                            "The error message was as follows:\n" +e.getMessage());
        }
    }

    // TODO: Initialize Planning events here. Planning Events not yet implemented, so do that first.
}

// Old base initStartOnly code
/** try {
 EventTime time = new EventTime(parent, timeCal);
 type = SOS;
 } catch (EventTypeMismatchException e) {
 throw new EventTypeMismatchException
 ("Somehow, you've managed to force this to make an error (StaticEvent.initStartOnly). " +
 "The error message was as follows:\n" + e.getMessage());
 }
 // Placeholder to make the errors go away - remove when complete
 return new StaticEvent();
 } */