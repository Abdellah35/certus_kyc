package com.softedge.solution.exceptionhandlers.custom.Utilities;

import com.softedge.solution.exceptionhandlers.BaseException;
import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;

import java.util.Map;

public class FileStorageException extends BaseException {
    private static final long serialVersionUID = 88256725033191L;

    public FileStorageException(ErrorCodeEnum errorCode) {
        this.setErrorCode(errorCode);
        this.setDebugMessage(errorCode.getName());
    }

    public FileStorageException(ErrorCodeEnum errorCode, String debugMessage) {
        super(debugMessage);
        this.setErrorCode(errorCode);
        this.setDebugMessage(debugMessage);
    }

    public FileStorageException(ErrorCodeEnum errorCode, String debugMessage, Throwable originalException) {
        super(originalException);
        this.setErrorCode(errorCode);
        this.setDebugMessage(debugMessage);
    }

    public FileStorageException(ErrorCodeEnum errorCode, String debugMessage, Map<String, String> messageArgs) {
        this.setErrorCode(errorCode);
        this.setDebugMessage(debugMessage);
        this.messageArgs = messageArgs;
    }

}
