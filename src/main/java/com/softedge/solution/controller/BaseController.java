package com.softedge.solution.controller;


import com.softedge.solution.exceptionhandlers.ServiceError;
import com.softedge.solution.exceptionhandlers.custom.company.CompanyServiceModuleException;
import com.softedge.solution.exceptionhandlers.custom.company.CompanyValidationModuleException;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserServiceModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserValidationModuleException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({SecurityException.class})
    public ServiceError handleSecurityException(SecurityException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.UNAUTHORIZED.value());
        error.setErrorCode("E00039");
        error.setMessage(ossEx.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserServiceModuleException.class})
    public ServiceError userProcessModuleException(UserServiceModuleException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrorCode(ossEx.getErrorCode().getErrorCode());
        error.setMessageArgs(ossEx.getMessageArgs());
        error.setMessage(StringUtils.isBlank(ossEx.getDebugMessage()) ? ossEx.getMessage() : ossEx.getDebugMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserValidationModuleException.class})
    public ServiceError userValidationModuleException(UserValidationModuleException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrorCode(ossEx.getErrorCode().getErrorCode());
        error.setMessageArgs(ossEx.getMessageArgs());
        error.setMessage(StringUtils.isBlank(ossEx.getDebugMessage()) ? ossEx.getMessage() : ossEx.getDebugMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CompanyServiceModuleException.class})
    public ServiceError companyLicenseProcessModuleException(CompanyServiceModuleException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrorCode(ossEx.getErrorCode().getErrorCode());
        error.setMessageArgs(ossEx.getMessageArgs());
        error.setMessage(StringUtils.isBlank(ossEx.getDebugMessage()) ? ossEx.getMessage() : ossEx.getDebugMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CompanyValidationModuleException.class})
    public ServiceError companyLicenseValidationModuleException(CompanyValidationModuleException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrorCode(ossEx.getErrorCode().getErrorCode());
        error.setMessageArgs(ossEx.getMessageArgs());
        error.setMessage(StringUtils.isBlank(ossEx.getDebugMessage()) ? ossEx.getMessage() : ossEx.getDebugMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAccountModuleException.class})
    public ServiceError userAccountModuleException(UserAccountModuleException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrorCode(ossEx.getErrorCode().getErrorCode());
        error.setMessageArgs(ossEx.getMessageArgs());
        error.setMessage(StringUtils.isBlank(ossEx.getDebugMessage()) ? ossEx.getMessage() : ossEx.getDebugMessage());
        return error;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({KycDocumentGenericModuleException.class})
    public ServiceError kycDocumentMetaModuleException(KycDocumentGenericModuleException ossEx) {
        log.error("Service Error:", ossEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrorCode(ossEx.getErrorCode().getErrorCode());
        error.setMessageArgs(ossEx.getMessageArgs());
        error.setMessage(StringUtils.isBlank(ossEx.getDebugMessage()) ? ossEx.getMessage() : ossEx.getDebugMessage());
        return error;
    }


    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({Throwable.class})
    public ServiceError handleRuntimeException(Throwable tEx) {
        log.error("Runtime Error:", tEx);
        ServiceError error = new ServiceError();
        error.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        error.setErrorCode("CTS5000");
        error.setMessage(tEx.getMessage());
        return error;
    }
}
