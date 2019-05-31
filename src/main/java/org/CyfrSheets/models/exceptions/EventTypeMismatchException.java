package org.CyfrSheets.models.exceptions;

/** Manage things such as events tagged as startOnly also
 * having ending times. Pulled from EventTime */
public class EventTypeMismatchException extends Exception {
    public EventTypeMismatchException(String s) { super(s); }
}