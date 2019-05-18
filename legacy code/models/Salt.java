package org.CyfrSheets.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Salt {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Participant participant;

    private byte[] saltHash;

    public Salt(byte[] saltHash) { this.saltHash = saltHash; }

    public Salt() { }

    public int getId() { return id; }
    public byte[] getSaltHash() { return saltHash; }
    public Participant getParticipant() { return participant; }

    public void setSaltHash(byte[] saltHash) { this.saltHash = saltHash; }
    public void setParticipant(Participant participant) { this.participant = participant; }
}
