package com.myCompany.gymBro.persistence.enums;

public enum NumberOfClasses {
    NONE(0),
    ONE(1),
    TWO(2),
    THREE(3),
    ALL(Integer.MAX_VALUE);

    private final int value;

    NumberOfClasses(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
