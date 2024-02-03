package com.softedge.solution.exceptionhandlers;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceError {

    private int httpStatus;
    private int serviceId;
    private String errorCode;
    private String message;
    private Map<String, String> messageArgs = new LinkedHashMap();


    public ServiceError() {
    }

    public ServiceError(int httpStatus, BaseException baseException) {
        this.httpStatus = httpStatus;
        this.serviceId = baseException.getErrorCode().getServiceId();
        this.errorCode = baseException.getErrorCode().getErrorCode();
        this.message = baseException.getDebugMessage();
        this.messageArgs = baseException.getMessageArgs();
    }

    public int getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getMessageArgs() {
        return this.messageArgs;
    }

    public void setMessageArgs(Map<String, String> messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String toString() {
        return "ServiceError [httpStatus=" + this.httpStatus + ", serviceId=" + this.serviceId + ", errorCode=" + this.errorCode + ", debugMessage=" + this.message + ", messageArgs=" + this.messageArgs + "]";
    }

}
