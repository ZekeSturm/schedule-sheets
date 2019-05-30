package org.CyfrSheets.models;

public enum EnuMonth {
    JANUARY ("January", 1),
    FEBRUARY ("February", 2),
    MARCH ("March", 3),
    APRIL ("April", 4),
    MAY ("May", 5),
    JUNE ("June", 6),
    JULY ("July", 7),
    AUGUST ("August", 8),
    SEPTEMBER ("September", 9),
    OCTOBER ("October", 10),
    NOVEMBER ("November", 11),
    DECEMBER ("December", 12);

    private final String monthName;
    private final int monthNum;

    EnuMonth(String name, int num) {
        this.monthName = name;
        this.monthNum = num;
    }

    public String getName() {
        return monthName;
    }

    public int getNum() {
        return monthNum;
    }
}
