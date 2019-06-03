package org.CyfrSheets.models;

public interface Participant {

    boolean registered();

    String getName();

    boolean isEqual(Participant p);

    boolean checkName(String name);

}
