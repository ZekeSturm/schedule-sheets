package org.CyfrSheets.models.utilities;

public class Sanitizer {

    private String buffer;

    public Sanitizer() { buffer = ""; }

    public boolean validUser(String toBe) {
        char[] toBeA = toBe.toCharArray();
        for (char c : toBeA) {
            if (!validUserASCII(c)) {
                return false;
            }
        }
        return true;
    }

    public String sanitizeSQL(String toBe) {
        buffer = "";
        char[] toBeA = toBe.toCharArray();
        for (char c : toBeA) {
            if (escapable(c)) {
                buffer += "\\";
            }
            buffer += c;
        }
        return buffer;
    }

    public String decodeSanitizedSQL(String toBe) {
        buffer = "";
        char[] toBeA = toBe.toCharArray();
        boolean escaped = false;
        for (char c : toBeA) {
            if (c == '\\' && !escaped) {
                escaped = true;
                continue;
            }
            buffer += c;
            escaped = false;
        }
        return buffer;
    }

    private boolean escapable(char c) {
        if (c == '-' || c == '_' ) {
            return true;
        }
        return false;
    }

    private boolean validUserASCII(char c) {
        if (c == '-') { return true; }
        if (c == '_') { return true; }
        int cI = (int) c;
        if (cI > 47 && cI < 58) { return true; }
        if (cI > 65 && cI < 90) { return true; }
        if (cI > 97 && cI < 112) { return true; }
        return false;
    }

}
