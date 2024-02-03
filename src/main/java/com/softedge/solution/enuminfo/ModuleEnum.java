package com.softedge.solution.enuminfo;

public enum ModuleEnum implements EnumCode {
    KYC_MODULE("KYC_MODULE"),
    DIGITAL_IPV("DIGITAL_IPV");

    private String name;

    ModuleEnum(String name) {
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
