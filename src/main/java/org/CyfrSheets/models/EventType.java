package org.CyfrSheets.models;

public enum EventType {
    SDS("Single Day, Static", true, false),
    SDP ("Single Day, Planning", true, true),
    MDS ("Multi Day, Static", false, true),
    MDP ("Multi Day, Planning", true, true);

    private final String typeString;
    private final boolean oneDay;
    private final boolean planning;

    EventType(String name, boolean oneDay, boolean planning) {
        this.typeString = name;
        this.oneDay = oneDay;
        this.planning = planning;
    }

    public String getType() { return typeString; }

    public boolean isOneDay() { return oneDay; }

    public boolean isPlanning() { return planning; }
}
