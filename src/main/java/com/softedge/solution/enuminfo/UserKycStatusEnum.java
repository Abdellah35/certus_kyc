package com.softedge.solution.enuminfo;

public enum UserKycStatusEnum implements EnumCode {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REQUESTED("REQUESTED"),
    REJECTED("REJECTED");

    private String name;

    UserKycStatusEnum(String name) {
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
