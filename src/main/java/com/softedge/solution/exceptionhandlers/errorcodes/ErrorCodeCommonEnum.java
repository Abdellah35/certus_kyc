package com.softedge.solution.exceptionhandlers.errorcodes;


import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;
import com.softedge.solution.exceptionhandlers.ServiceEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ErrorCodeCommonEnum implements ErrorCodeEnum {

    INTERNAL_SERVER_ERROR("USER099", "Internal Error, Please contact technical team"),
    UNAUTHORIZED("USER100", "UnAuthorized");

    private static final Map<String, ErrorCodeCommonEnum> LOOKUP = new HashMap();
    private static ServiceEnum serviceEnum;
    private String errorCode;
    private String name;

    ErrorCodeCommonEnum(String errorCode, String name) {
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

    public static ErrorCodeCommonEnum get(String errorCode) {
        return (ErrorCodeCommonEnum) LOOKUP.get(errorCode);
    }

    public String getEnumName() {
        return this.name();
    }

    static {
        Iterator var0 = EnumSet.allOf(ErrorCodeCommonEnum.class).iterator();

        while (var0.hasNext()) {
            ErrorCodeCommonEnum e = (ErrorCodeCommonEnum) var0.next();
            LOOKUP.put(e.getErrorCode(), e);
        }

        serviceEnum = ServiceEnum.FILEGENERATOR;
    }
}
