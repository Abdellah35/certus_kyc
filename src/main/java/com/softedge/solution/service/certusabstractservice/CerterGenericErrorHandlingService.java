package com.softedge.solution.service.certusabstractservice;

import com.softedge.solution.exceptionhandlers.BaseException;
import com.softedge.solution.exceptionhandlers.custom.Utilities.FileStorageException;
import com.softedge.solution.exceptionhandlers.custom.company.CompanyServiceModuleException;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeCompanyEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUserEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeCompanyEnum.KYC_ID_EMPTY;
import static com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum.*;
import static com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUserEnum.*;

public class CerterGenericErrorHandlingService<T extends BaseException>  {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public T errorService(String errorCode, Exception e) throws UserAccountModuleException, FileStorageException, KycDocumentGenericModuleException {
        logger.error("Exception -> {},{}", e.getMessage(), e);
        return this.serviceException(errorCode);
    }

    public T errorService(String errorCode) throws UserAccountModuleException, FileStorageException, KycDocumentGenericModuleException {
        return this.serviceException(errorCode);
    }


    private T serviceException(String errorCode) throws UserAccountModuleException, FileStorageException, KycDocumentGenericModuleException {
        if (errorCode != null == USER_ID_EXISTS.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_ID_EXISTS, USER_ID_EXISTS.getName());
        } else if (errorCode != null == USER_ACTIVATION_NULL.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_ACTIVATION_NULL, USER_ACTIVATION_NULL.getName());
        } else if (errorCode != null == USER_ACTIVATION_OTP.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_ACTIVATION_OTP, USER_ACTIVATION_OTP.getName());
        } else if (errorCode != null == ErrorCodeUserEnum.USER_PASSWORD_EMPTY.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_PASSWORD_EMPTY, ErrorCodeUserEnum.USER_PASSWORD_EMPTY.getName());
        } else if (errorCode != null == USER_ID_EMPTY.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_ID_EMPTY, USER_ID_EMPTY.getName());
        } else if (errorCode != null == USER_GENDER_INVALID.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_GENDER_INVALID, USER_GENDER_INVALID.getName());
        } else if (errorCode != null == USER_DOB_INVALID.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(USER_DOB_INVALID, USER_DOB_INVALID.getName());
        } else if (errorCode != null == EMPTY_DOCUMENTS.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(EMPTY_DOCUMENTS, EMPTY_DOCUMENTS.getName());
        } else if (errorCode != null == KYC_ID_EMPTY.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(KYC_ID_EMPTY, EMPTY_DOCUMENTS.getName());
        } else if (errorCode != null == DELETE_FAILED.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(DELETE_FAILED, EMPTY_DOCUMENTS.getName());
        } else if (errorCode != null == DOCUMENT_NOT_FOUNT.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(DOCUMENT_NOT_FOUNT, DOCUMENT_NOT_FOUNT.getName());
        } else if (errorCode != null ==DATA_CONNECTION_LOST.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(DATA_CONNECTION_LOST, DATA_CONNECTION_LOST.getName());
        } else if (errorCode != null == SQL_GRAMMER_ISSUE.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(SQL_GRAMMER_ISSUE, SQL_GRAMMER_ISSUE.getName());
        } else if (errorCode != null == DATA_INCORRECT.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(DATA_INCORRECT, DATA_INCORRECT.getName());
        }else if (errorCode != null == COMPANY_ID_INVALID.getErrorCode().equals(errorCode)) {
            return (T) new CompanyServiceModuleException(COMPANY_ID_INVALID, COMPANY_ID_INVALID.getName());
        }else if (errorCode != null == INVALID_FORM.getErrorCode().equals(errorCode)) {
            return (T) new UserAccountModuleException(INVALID_FORM, INVALID_FORM.getName());
        } else if (errorCode != null == ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode().equals(errorCode)) {
            return (T) new KycDocumentGenericModuleException(ErrorCodeKYCEnum.UNAUTHORIZED, ErrorCodeKYCEnum.UNAUTHORIZED.getName());
        }
        return (T) new UserAccountModuleException(ErrorCodeUserEnum.INTERNAL_SERVER_ERROR, ErrorCodeUserEnum.INTERNAL_SERVER_ERROR.getName());

    }
}
