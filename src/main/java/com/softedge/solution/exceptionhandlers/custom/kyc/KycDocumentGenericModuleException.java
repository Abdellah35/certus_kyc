package com.softedge.solution.exceptionhandlers.custom.kyc;

import com.softedge.solution.exceptionhandlers.BaseException;
import com.softedge.solution.exceptionhandlers.ErrorCodeEnum;

import java.util.Map;

public class KycDocumentGenericModuleException extends BaseException {

    private static final long serialVersionUID = 88256725033191L;

    public KycDocumentGenericModuleException(ErrorCodeEnum errorCode) {
        this.setErrorCode(errorCode);
        this.setDebugMessage(errorCode.getName());
    }

    public KycDocumentGenericModuleException(ErrorCodeEnum errorCode, String debugMessage) {
        super(debugMessage);
        this.setErrorCode(errorCode);
        this.setDebugMessage(debugMessage);
    }

    public KycDocumentGenericModuleException(ErrorCodeEnum errorCode, String debugMessage, Throwable originalException) {
        super(originalException);
        this.setErrorCode(errorCode);
        this.setDebugMessage(debugMessage);
    }

    public KycDocumentGenericModuleException(ErrorCodeEnum errorCode, String debugMessage, Map<String, String> messageArgs) {
        this.setErrorCode(errorCode);
        this.setDebugMessage(debugMessage);
        this.messageArgs = messageArgs;
    }
}
