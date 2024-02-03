package com.softedge.solution.exceptionhandlers.errorcodes;


import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;
import com.softedge.solution.exceptionhandlers.ServiceEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ErrorCodeUtilityEnum implements ErrorCodeEnum {

    FILE_NOT_FOUND("FILE001", "File not found"),
    FILE_INVALID_PATH_SEQUENCE("FILE002", "Filename contains invalid path sequence"),
    FILE_STORE_FAILED("FILE003", "Could not store file"),
    FILE_INTERNAL_ERROR("FILE099", "File internal error");


    private static final Map<String, ErrorCodeUtilityEnum> LOOKUP = new HashMap();
    private static ServiceEnum serviceEnum;
    private String errorCode;
    private String name;

    ErrorCodeUtilityEnum(String errorCode, String name) {
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
        switch (this) {
            case FILE_NOT_FOUND:
                return this.getName();
            case FILE_INVALID_PATH_SEQUENCE:
                return this.getName();
            case FILE_STORE_FAILED:
                return this.getName();
            default:
                return "We are not able to process your request at this time. Please contact the technical support";
        }
    }

    public static ErrorCodeUtilityEnum get(String errorCode) {
        return (ErrorCodeUtilityEnum) LOOKUP.get(errorCode);
    }

    public String getEnumName() {
        return this.name();
    }

    static {
        Iterator var0 = EnumSet.allOf(ErrorCodeUtilityEnum.class).iterator();

        while (var0.hasNext()) {
            ErrorCodeUtilityEnum e = (ErrorCodeUtilityEnum) var0.next();
            LOOKUP.put(e.getErrorCode(), e);
        }

        serviceEnum = ServiceEnum.FILEGENERATOR;
    }
}
