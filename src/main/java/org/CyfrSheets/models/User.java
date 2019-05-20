package org.CyfrSheets.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Entity
public class User extends Participant {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Size(min = 5)
    private String email;

    private byte[] secPass;

    // See participant about making these unique later
    private byte[] salt;

    private boolean valid;

    public User(String name, String pass, String email) throws NoSuchAlgorithmException {
        this.name = name;
        this.email = email;
        try {
            securePassword(pass);
            valid = true;
        } catch (NoSuchAlgorithmException e) {
            valid = false;
        }
        valid = this.name.isEmpty();
    }

    public User() { }

    @Override
    public boolean isUser() { return true; }

    public boolean checkID(int id) { return this.id == id; }

    public boolean checkName(String name) { return this.name.toLowerCase().equals(name.toLowerCase()); }

    public void setSecPass(byte[] secPass) { this.secPass = secPass; }

    public String getEmail() { return email; }

    public boolean attemptChangeEmail(String email, String pass) {
        if (checkPassword(pass)) {
            changeEmail(email);
            return true;
        }
        return false;
    }

    public boolean attemptChangePass(String pass, String newPass) {
        if (checkPassword(pass)) {
            return changePassword(newPass);
        }
        return false;
    }

    public boolean isValid() { return valid; }

    private void changeEmail(String email) {
        this.email = email;
    }

    private boolean changePassword(String pass) {
        try {
            securePassword(pass);
            return true;
        } catch (NoSuchAlgorithmException e) {
            System.out.print("Fucked up");
            return false;
        }
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
