package com.softedge.solution.exceptionhandlers.errorcodes;


import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;
import com.softedge.solution.exceptionhandlers.ServiceEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ErrorCodeKYCEnum implements ErrorCodeEnum {

    EMPTY_DOCUMENTS("KYC001", "No documents found"),
    DOCUMENT_NOT_FOUNT("KYC002", "Document not found"),
    DATA_CONNECTION_LOST("KYC003", "Data connection lost... Try again later"),
    SQL_GRAMMER_ISSUE("KYC004", "Data Security issue, cannot process for safety reasons... Try again later"),
    DATA_INCORRECT("KYC005", "Data incorrect... Please try again later"),
    NOTIFICATION_FAILUER("KYC006", "Notification failed to save"),
    DELETE_FAILED("KYC007", "Cannot delete kyc as the process state is beyond: REQUESTED"),
    INTERNAL_SERVER_ERROR("USER099", "Internal Error, Please contact technical team"),

    UNAUTHORIZED("USER100", "UnAuthorized");

    private static final Map<String, ErrorCodeKYCEnum> LOOKUP = new HashMap();
    private static ServiceEnum serviceEnum;
    private String errorCode;
    private String name;

    ErrorCodeKYCEnum(String errorCode, String name) {
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

    public static ErrorCodeKYCEnum get(String errorCode) {
        return (ErrorCodeKYCEnum) LOOKUP.get(errorCode);
    }

    public String getEnumName() {
        return this.name();
    }

    static {
        Iterator var0 = EnumSet.allOf(ErrorCodeKYCEnum.class).iterator();

        while (var0.hasNext()) {
            ErrorCodeKYCEnum e = (ErrorCodeKYCEnum) var0.next();
            LOOKUP.put(e.getErrorCode(), e);
        }

        serviceEnum = ServiceEnum.FILEGENERATOR;
    }
}
