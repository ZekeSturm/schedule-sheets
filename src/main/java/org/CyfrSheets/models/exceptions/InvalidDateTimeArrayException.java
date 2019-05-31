package org.CyfrSheets.models.exceptions;

/** Handle strings that do not produce valid data for insertion
 * into Calendar objects. Pulled from NewEventForm */
public class InvalidDateTimeArrayException extends Exception {
    public InvalidDateTimeArrayException(String s) { super(s); }
}