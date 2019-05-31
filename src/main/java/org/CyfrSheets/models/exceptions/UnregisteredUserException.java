package org.CyfrSheets.models.exceptions;

/** Checks for user validation when attempting to modify
 * events. Pulled from SEvent */
public class UnregisteredUserException extends Exception {
    public UnregisteredUserException(String s) { super(s); }
}
