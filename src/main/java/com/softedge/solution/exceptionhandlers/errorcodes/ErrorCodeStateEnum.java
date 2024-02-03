package com.softedge.solution.exceptionhandlers.errorcodes;


import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;
import com.softedge.solution.exceptionhandlers.ServiceEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ErrorCodeStateEnum implements ErrorCodeEnum {

    STATE_INVALID("STA001", "State is invalid");

    private static final Map<String, ErrorCodeStateEnum> LOOKUP = new HashMap();
    private static ServiceEnum serviceEnum;
    private String errorCode;
    private String name;

    ErrorCodeStateEnum(String errorCode, String name) {
        this.errorCode = errorCode;
        this.name = name;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getName() {
        return this.name;
    }

    public int getServiceId() {
        return serviceEnum.getServiceId();
    }

    public String getMessageKey() {
        return this.name;
    }

    public String getDefaultMessage() {
        return "We are not able to process your request at this time. Please contact the technical support";
    }

    public static ErrorCodeStateEnum get(String errorCode) {
        return (ErrorCodeStateEnum) LOOKUP.get(errorCode);
    }

    public String getEnumName() {
        return this.name();
    }

    static {
        Iterator var0 = EnumSet.allOf(ErrorCodeStateEnum.class).iterator();

        while (var0.hasNext()) {
            ErrorCodeStateEnum e = (ErrorCodeStateEnum) var0.next();
            LOOKUP.put(e.getErrorCode(), e);
        }

        serviceEnum = ServiceEnum.FILEGENERATOR;
    }
}
