package org.CyfrSheets.models;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Participant {

    @NotNull
    private String name;

    @OneToOne
    private PassHash secPass = null;

    @OneToOne
    private byte[] salt = null;

    private SaltList saltList;


    public Participant(String name, String pass) {
        saltList = saltList.getInstance();
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

    public boolean hasPass() {
        return secPass == null;
    }

    public boolean changePassword(String pass) {
        if ( !saltList.removeSalt(salt) ) {
            return false;
        }
        try {
            securePassword(pass);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the hell did you even get this error? What did you break? What did you <i>do?!</i> You fucked up the hash algorithm somehow, gave it anxiety.");
            return false;
        }
        return true;
    }

    private void securePassword(String pass) throws NoSuchAlgorithmException {
        if ( pass == null ) { return; }
        getSalt();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        SecureRandom sR = new SecureRandom();

        md.update(salt);
        secPass = new PassHash(md.digest(pass.getBytes()));
    }

    private void getSalt() throws NoSuchAlgorithmException {
        saltList.removeSalt(salt);
        SecureRandom sR = SecureRandom.getInstance("SHA1PRNG");
        salt = new byte[32];
        sR.nextBytes(salt);
        saltList.addSalt(salt);
    }
}
