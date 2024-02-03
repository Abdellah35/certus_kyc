package com.softedge.solution.enuminfo;

public enum ProcessStatusEnum implements EnumCode {
    REQUESTED("REQUESTED"),
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    SUBMITTED("SUBMITTED"), // For internal notification purpose
    REJECTED("REJECTED");

    private String name;

    ProcessStatusEnum(String name) {
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
