package org.CyfrSheets.models.exceptions;

/** Handle missing data fields in the event builder.
 * Pulled from NewEventForm. */
public class InsufficientEventDataException extends Exception {
    public InsufficientEventDataException(String s) { super(s); }
}