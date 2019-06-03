package org.CyfrSheets.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TempUser implements Participant {

    private String name;

    // In case these need to be made persistent - clear this out and remove from constructors if unused by MVP
    private SEvent parent;

    private byte[] secPass = null;

    // Figure out way to enforce unique hashes for this later
    private byte[] salt = null;

    public TempUser(SEvent parent, String name, String pass) {
        this.name = name;
        this.parent = parent;
        try {
            securePassword(pass);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the hell did you even get this error? What did you break? What did you <i>do?!</i> You fucked up the hash algorithm somehow, gave it anxiety.");
        }
    }

    public TempUser(SEvent parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public TempUser() { }

    public boolean registered() { return false; }

    public boolean isEqual(Participant p) {
        if (p.registered()) return false;
        return checkName(p.getName());
    }

    public String getName() { return name; }

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
