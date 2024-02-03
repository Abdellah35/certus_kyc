package com.softedge.solution.exceptionhandlers;

public interface ErrorCodeEnum {

    String getErrorCode();

    String getName();

    int getServiceId();

    String getMessageKey();

    String getDefaultMessage();

    String getEnumName();
}
