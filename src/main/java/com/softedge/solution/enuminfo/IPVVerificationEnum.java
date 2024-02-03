package com.softedge.solution.enuminfo;

public enum IPVVerificationEnum implements EnumCode {

    REQUESTED("Requested"),
    IN_PROGRESS("In-Progress"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");

    private String name;

    IPVVerificationEnum(String name) {
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
