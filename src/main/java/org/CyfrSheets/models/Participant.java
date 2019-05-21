package org.CyfrSheets.models;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Participant {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @ManyToMany
    private List<Event> events = new ArrayList<>();

    private byte[] secPass = null;

    // Figure out way to enforce unique hashes for this later
    private byte[] salt = null;

    // Makeshift validation
    private boolean isValid;

    public Participant(String name, String pass) {
        this.name = name;
        try {
            securePassword(pass);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the hell did you even get this error? What did you break? What did you <i>do?!</i> You fucked up the hash algorithm somehow, gave it anxiety.");
        }
    }

    public Participant(String name) {
        this.name = name;
    }

    public Participant() { }

    public boolean isUser() { return false; }

    public boolean checkName(String name) { return this.name.toLowerCase().equals(name.toLowerCase()); }

    public boolean hasPass() {
        return secPass == null;
    }

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

    public String getEmail() { return "this is in no way a valid email, I hope"; }

    public boolean isValid() { return isValid; }

    private void securePassword(String pass) throws NoSuchAlgorithmException {
        if ( pass == null ) { return; }
        getSalt();
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(salt);
        secPass = md.digest(pass.getBytes());
    }

    private void getSalt() throws NoSuchAlgorithmException {
        SecureRandom sR = SecureRandom.getInstance("SHA1PRNG");
        byte[] saltByte = new byte[32];
        sR.nextBytes(saltByte);
        salt = saltByte;
    }

    private boolean isHash(byte[] hash) {
        if (secPass.length != hash.length) { return false; }
        for (int i = 0; i < hash.length; i++) {
            if (secPass[i] != hash[i]) {
                return false;
            }
        }
        return true;
    }
}
