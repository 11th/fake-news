package ru.eleventh.fakenews.models;

public enum Rubric {
    DEFAULT(""),
    SPORT("Sport"),
    FINANCE("Finance"),
    POLITICS("Politics"),
    TRAVELING("Traveling");

    private final String displayValue;

    private Rubric(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
