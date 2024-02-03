package com.softedge.solution.enuminfo;

public enum GenderEnum implements EnumCode {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHERS("OTHERS");

    private String name;

    GenderEnum(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name;
    }

    public String getEnumName() {
        return this.name();
    }
}
