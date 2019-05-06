package org.CyfrSheets.models;

public enum Month {
    JANUARY     (1),
    FEBRUARY    (2),
    MARCH       (3),
    APRIL       (4),
    MAY         (5),
    JUNE        (6),
    JULY        (7),
    AUGUST      (8),
    SEPTEMBER   (9),
    OCTOBER     (10),
    NOVEMBER    (11),
    DECEMBER    (12);

    private int numerical;
    Month(int num) {
        this.numerical = num;
    }

    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }
}
