package com.softedge.solution.exceptionhandlers;

import java.util.LinkedHashMap;
import java.util.Map;


public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -896190511341174L;
    protected String message;
    protected ErrorCodeEnum errorCode;
    protected boolean isSevere = false;
    protected String debugMessage;
    protected Map<String, String> messageArgs = new LinkedHashMap();

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(Throwable originalException) {
        super(originalException);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCodeEnum getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(ErrorCodeEnum errorCode) {
        this.errorCode = errorCode;
    }

    public String getDebugMessage() {
        return this.debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public Map<String, String> getMessageArgs() {
        return this.messageArgs;
    }

    public void setMessageArgs(Map<String, String> messageArgs) {
        this.messageArgs = messageArgs;
    }

    public boolean isSevere() {
        return this.isSevere;
    }

    public void setSevere(boolean isSevere) {
        this.isSevere = isSevere;
    }
}

