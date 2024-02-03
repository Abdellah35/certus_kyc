package com.softedge.solution.exceptionhandlers.errorcodes;


import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;
import com.softedge.solution.exceptionhandlers.ServiceEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ErrorCodeCompanyEnum implements ErrorCodeEnum {

    INVALID_FORM("COMPLIC001", "Invalid form Data to Process"),
    COMPANY_ID_INVALID("COMPLIC002", "Invalid Company Id"),
    COMPANY_DOMAIN_NAME_EXISTS("COMPLIC003", "Company Domain Name Exists"),
    COMPANY_NAME_EXISTS("COMPLIC004", "Company Domain Name Exists"),
    COMPANY_LICENSE_PROCESS_ERROR("COMPLIC005", "Process execution Error Contact technical Support"),
    INTERNAL_SERVER_ERROR("COMPLIC006", "Internal Error, Contact technical Support"),
    DATA_ACCESS_ISSUE("COMPLIC007", "Data Access issue, Contact technical Support"),
    DEFAULT_LICENSE_CREATION_ERROR("COMPLIC008", "Failed to create Default license, Contact technical Support"),
    COMPANY_NAME_MANDATORY("COMPLIC010", "Company Name Mandatory"),
    COMPANY_URL_MANDATORY("COMPLIC011", "Company URL Mandatory"),
    DOMAIN_NAME_MANDATORY("COMPLIC012", "Domain Name Mandatory"),
    CATEGORY_MANDATORY("COMPLIC013", "Company Mandatory"),
    DOMAIN_NAME_INVALID("COMPLIC014", "Domain name invalid"),
    UNAUTHORIZED("COMPLIC100", "Unauthorised to perform this operation"),
    KYC_ID_EMPTY("COMPLIC015", "KYC Id is Empty and cannot process the operation. Please try again later");

    private static final Map<String, ErrorCodeCompanyEnum> LOOKUP = new HashMap<>();
    private static ServiceEnum serviceEnum;
    private String errorCode;
    private String name;

    ErrorCodeCompanyEnum(String errorCode, String name) {
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
        if (this.equals(errorCode)) {
            return getName();
        } else {
            return "We are not able to process your request at this time. Please contact the technical support";
        }
    }

    public static ErrorCodeCompanyEnum get(String errorCode) {
        return (ErrorCodeCompanyEnum) LOOKUP.get(errorCode);
    }

    public String getEnumName() {
        return this.name();
    }

    static {
        for (ErrorCodeCompanyEnum e : EnumSet.allOf(ErrorCodeCompanyEnum.class)) {
            LOOKUP.put(e.getErrorCode(), e);
        }
        serviceEnum = ServiceEnum.FILEGENERATOR;
    }
}
