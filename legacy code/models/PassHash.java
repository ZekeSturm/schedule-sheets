package org.CyfrSheets.models;

public class PassHash {



    private byte[] hash;

    private Participant participant;

    public PassHash(byte[] hash) { this.hash = hash; }

    public byte[] getHash() { return hash; }

    public boolean isHash(byte[] hash) {
        if (this.hash.length != hash.length) { return false; }
        for (int i = 0; i < hash.length; i++) {
            if (this.hash[i] != hash[i]) {
                return false;
            }
        }
        return true;
    }
}
