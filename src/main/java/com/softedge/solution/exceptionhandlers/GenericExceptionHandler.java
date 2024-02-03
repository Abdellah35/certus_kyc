package com.softedge.solution.exceptionhandlers;

import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.service.certusabstractservice.CerterGenericErrorHandlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.sql.SQLException;

@Service
public class GenericExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    public static KycDocumentGenericModuleException exceptionHandler(Object exception, Class c) throws KycDocumentGenericModuleException {
        if (exception instanceof DataAccessException) {
            repoExceptionHandling((DataAccessException)exception, c);
        }else if(exception instanceof SQLDataException){
            repoExceptionHandling((DataAccessException)exception, c);
        }else if(exception instanceof EmptyResultDataAccessException){
            repoExceptionHandling((EmptyResultDataAccessException)exception, c);
        }else if(exception instanceof SQLException){
            repoExceptionHandling((SQLException)exception, c);
        }else if(exception instanceof IllegalArgumentException){
            repoExceptionHandling((IllegalArgumentException)exception, c);
        }else if(exception instanceof Exception){
            throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.INTERNAL_SERVER_ERROR.getErrorCode(),(Exception) exception);
        }
        throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.INTERNAL_SERVER_ERROR.getErrorCode(),(Exception) exception);
    }

     private static void repoExceptionHandling(DataAccessException e, Class c) throws KycDocumentGenericModuleException {
         logger.error("Error is -> {} and the class name is {}", e, c.getClass());
         throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.SQL_GRAMMER_ISSUE.getErrorCode(),e);
    }
    private static void repoExceptionHandling(EmptyResultDataAccessException e, Class c) throws KycDocumentGenericModuleException {
        logger.error("Error is -> {} and the class name is {}", e, c.getClass());
        throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.EMPTY_DOCUMENTS.getErrorCode(),e);
    }

    private static void repoExceptionHandling(SQLDataException e, Class c) throws KycDocumentGenericModuleException {
        logger.error("Error is -> {} and the class name is {}", e, c.getClass());
        throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.DATA_INCORRECT.getErrorCode(),e);
    }

    private static void repoExceptionHandling(SQLException e, Class c) throws KycDocumentGenericModuleException {
        logger.error("Error is -> {} and the class name is {}", e, c.getClass());
        throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.INTERNAL_SERVER_ERROR.getErrorCode(),e);
    }
    private static void repoExceptionHandling(IllegalArgumentException e, Class c) throws KycDocumentGenericModuleException {
        logger.error("Error is -> {} and the class name is {}", e, c.getClass());
        throw new CerterGenericErrorHandlingService().errorService(ErrorCodeKYCEnum.INTERNAL_SERVER_ERROR.getErrorCode(),e);
    }
}
