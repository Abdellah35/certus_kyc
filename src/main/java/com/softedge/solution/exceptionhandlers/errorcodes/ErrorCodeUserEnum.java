package com.softedge.solution.exceptionhandlers.errorcodes;


import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;
import com.softedge.solution.exceptionhandlers.ServiceEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ErrorCodeUserEnum implements ErrorCodeEnum {

    USER_ROLE_RESTRICTED("USER001", "Restricted"),
    USER_VIEW_RESTRICTED("USER02", "View Restricted"),
    INVALID_FORM("USER003", "Invalid Form"),
    COMPANY_ID_MANDATORY("USER004", "Company Id mandatory"),
    EMAIL_ID_MANDATORY("USER005", "Mail Id mandatory"),
    USER_ACTIVATION_NULL("USER006", "User activation empty"),
    USER_CATEGORY_INVALID("USER007", "User Category Invalid"),
    USER_ROLES_MANDATORY("USER008", "User Roles Mandatory"),
    USER_ID_EXISTS("USER009", "User Id Exists"),
    COMPANY_ID_INVALID("USER010", "Company Id Invalid"),
    USER_ACTIVATION_OTP("USER011", "User activation OTP invalid OR expired"),
    USER_CATEGORY_MANDATORY("USER012", "User Category mandatory"),
    USER_PASSWORD_EMPTY("USER013", "User Password empty"),
    USER_ROLES_INVALID("USER014", "User Roles Invalid"),
    USER_ID_EMPTY("USER015", "User Id Empty"),
    USER_PROCESS_ERROR("USER016", "User Details data Error"),
    USER_GENDER_INVALID("USER017", "User gender is invalid"),
    USER_DOB_INVALID("USER017", "User Date of birth is invalid"),
    USER_NAME_MANDATORY("USER017", "name is mandatory"),
    USER_LOCATION_MANDATORY("USER017", "location information is mandatory"),

    INTERNAL_SERVER_ERROR("USER099", "Internal Error, Please contact technical team"),
    UNAUTHORIZED("USER100", "UnAuthorized");

    private static final Map<String, ErrorCodeUserEnum> LOOKUP = new HashMap();
    private static ServiceEnum serviceEnum;
    private String errorCode;
    private String name;

    ErrorCodeUserEnum(String errorCode, String name) {
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
            case USER_ROLE_RESTRICTED:
                return this.getName();
            case USER_VIEW_RESTRICTED:
                return this.getName();
            case INVALID_FORM:
                return this.getName();
            case COMPANY_ID_MANDATORY:
                return this.getName();
            case EMAIL_ID_MANDATORY:
                return this.getName();
            case USER_ACTIVATION_NULL:
                return this.getName();
            case USER_CATEGORY_INVALID:
                return this.getName();
            case USER_ROLES_MANDATORY:
                return this.getName();
            case USER_ID_EXISTS:
                return this.getName();
            case COMPANY_ID_INVALID:
                return this.getName();
            case USER_ACTIVATION_OTP:
                return this.getName();
            case USER_CATEGORY_MANDATORY:
                return this.getName();
            case USER_ROLES_INVALID:
                return this.getName();
            case INTERNAL_SERVER_ERROR:
                return this.getName();
            case USER_PROCESS_ERROR:
                return this.getName();
            case UNAUTHORIZED:
                return this.getName();
            case USER_ID_EMPTY:
                return this.getName();
            default:
                return "We are not able to process your request at this time. Please contact the technical support";
        }
    }

    public static ErrorCodeUserEnum get(String errorCode) {
        return (ErrorCodeUserEnum) LOOKUP.get(errorCode);
    }

    public String getEnumName() {
        return this.name();
    }

    static {
        Iterator var0 = EnumSet.allOf(ErrorCodeUserEnum.class).iterator();

        while (var0.hasNext()) {
            ErrorCodeUserEnum e = (ErrorCodeUserEnum) var0.next();
            LOOKUP.put(e.getErrorCode(), e);
        }

        serviceEnum = ServiceEnum.FILEGENERATOR;
    }
}
