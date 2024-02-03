package com.softedge.solution.enuminfo;

public enum CategoryEnum implements EnumCode {
    ADMIN("ADMIN"),
    CLIENT("CLIENT"),
    USER("USER");

    private String name;

    CategoryEnum(String name) {
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
