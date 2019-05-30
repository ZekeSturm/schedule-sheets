package org.CyfrSheets.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Participant {

    private String name = "Abstract Placeholder";

    private byte[] salt;

    public Participant () { }

    public boolean isUser() { return false; }
    public boolean hasPass() { return false; }

    public String getName() { return name; }

    public boolean isEqual(Participant p) { return false; }
    public boolean isEqual(User u) { return false; }

    public boolean checkName(String name) { return this.name.toLowerCase().equals(name.toLowerCase()); }

    public boolean checkPassword(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return isHash(md.digest(pass.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("You've really fucked it up this time, haven't you my dear?");
        }
        return false;
    }

    private boolean isHash(byte[] hash) { return false; }

}
