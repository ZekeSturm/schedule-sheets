package org.CyfrSheets.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TempUser extends Participant {

    private String name;

    private byte[] secPass = null;

    // Figure out way to enforce unique hashes for this later
    private byte[] salt = null;

    public TempUser(String name, String pass) {
        this.name = name;
        try {
            securePassword(pass);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the hell did you even get this error? What did you break? What did you <i>do?!</i> You fucked up the hash algorithm somehow, gave it anxiety.");
        }
    }

    public TempUser(String name) {
        this.name = name;
    }

    public TempUser() { }

    public boolean isUser() { return false; }

    public boolean checkName(String name) { return this.name.toLowerCase().equals(name.toLowerCase()); }

    public boolean hasPass() {
        return secPass == null;
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
