package org.CyfrSheets.models;

import javax.persistence.ManyToOne;

public class PassHash {

    private byte[] hash;

    public PassHash(byte[] hash) { this.hash = hash; }

    public byte[] getHash() { return hash; }

    public boolean isHash(byte[] hash) { return this.hash == hash; }
}
