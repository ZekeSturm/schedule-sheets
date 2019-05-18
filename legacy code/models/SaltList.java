package org.CyfrSheets.legacy.models;

import org.CyfrSheets.models.Participant;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class SaltList {

    private List<byte[]> saltList = new ArrayList<>();

    @OneToMany
    private List<Participant> participants = new ArrayList<>();

    private static SaltList instance;

    public SaltList() { }

    public SaltList getInstance() {
        if ( instance == null ) {
            instance = new SaltList();
        }
        return instance;
    }

    public boolean addSalt(byte[] salt) {
        if ( saltList.contains(salt) ) { return false; }
        saltList.add(salt);
        return true;
    }

    public boolean removeSalt(byte[] salt) {
        if ( saltList.contains(salt) ) {
            saltList.remove(salt);
            return true;
        }
        return false;
    }

    public boolean hasSalt(byte[] checkThis) { return saltList.contains(checkThis); }
}
