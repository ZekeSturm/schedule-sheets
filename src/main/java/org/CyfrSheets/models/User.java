package org.CyfrSheets.models;

import org.CyfrSheets.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User implements Participant {

    @Autowired
    private UserDao userDao;

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Size(min = 5)
    private String email;

    @ManyToMany(mappedBy = "regUsers")
    private List<SEvent> events;

    @NotNull
    private byte[] secPass;

    // See participant about making these unique later
    @NotNull
    private byte[] salt;

    private ArrayList<byte[]> cSaltList;
    private boolean cSaltListYN = false;

    // private boolean noNPE = true;

    private String npeLoc;

    public User(String name, String pass, String email) throws NoSuchAlgorithmException {
        this.name = name;
        this.email = email;
        try {
            securePassword(pass);
            // noNPE = true;
        } catch (NoSuchAlgorithmException e) {
            this.name = "";
            // noNPE = false;
            // hasNPE("Primary constructor");
        }
    }

    public User() { }

    public boolean registered() { return true; }

    public boolean checkID(int id) { return this.id == id; }

    public void setSecPass(byte[] secPass) { this.secPass = secPass; }

    public String getName() { return name; }

    public int getID() { return id; }

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

    public void passTheSalt(byte[] salt) {
        cSaltListInit();
        cSaltList.add(salt);
    }

    public ArrayList<byte[]> giveTheShaker(int eID) { return cSaltList; }

    // Check for NPE during construction. Broken rn.
    /**
    public String hasNPE(String method) {
        if (noNPE) {
            return "No Null Pointer Exception";
        }
        npeLoc = method;
        return "Null Pointer Exception in " + method;
    } */

    /**
     boolean hasNPE() {
        return !noNPE;
    } */

    public String getNpeLoc() { return npeLoc; }

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

    public boolean isEqual(Participant p) {
        if (!p.registered()) return false;
        User u = (User)p;
        if (!u.checkID(this.id)) return false;
        if (!u.checkName(this.name)) return false;
        if (!u.getEmail().equals(this.getEmail())) return false;
        return true;
    }

    public boolean checkName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
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

    private void cSaltListInit() {
        if (cSaltListYN) return;
        cSaltListYN = true;
        cSaltList = new ArrayList<>();
    }

    private void securePassword(String pass) throws NoSuchAlgorithmException {
        if ( pass == null ) { return; }
        getSalt();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        secPass = md.digest(pass.getBytes());
    }

    private void getSalt() throws NoSuchAlgorithmException {
        boolean unique = false;
        SecureRandom sR = SecureRandom.getInstance("SHA1PRNG");
        byte[] saltByte = new byte[32];
        while (!unique) {
            sR.nextBytes(saltByte);
            unique = !usedSalt(saltByte);
        }
        salt = saltByte;
    }

    private boolean usedSalt(byte[] salt) {
        for (User u : userDao.findAll()) {
            boolean same = true;
            for (int i = 0; i < salt.length; i++) {
                if (u.salt[i] != salt[i]) same = false;
            }
            if (same) return true;
        }
        return false;
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
