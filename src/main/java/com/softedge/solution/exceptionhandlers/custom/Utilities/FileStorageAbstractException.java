package com.softedge.solution.exceptionhandlers.custom.Utilities;

import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUtilityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FileStorageAbstractException {

    private final Logger logger = LoggerFactory.getLogger(FileStorageAbstractException.class);

    protected FileStorageException errorFileUtilityService(String errorCode, Exception e) throws FileStorageException {
        logger.error("Exception -> {},{}", e.getMessage(), e);
        return this.fileStorageUtilityException(errorCode);
    }

    protected FileStorageException errorFileUtilityService(String errorCode) throws FileStorageException {
        return this.fileStorageUtilityException(errorCode);
    }


    private FileStorageException fileStorageUtilityException(String errorCode) throws FileStorageException {
        switch (ErrorCodeUtilityEnum.get(errorCode)) {
            case FILE_NOT_FOUND:
                return new FileStorageException(ErrorCodeUtilityEnum.FILE_NOT_FOUND, ErrorCodeUtilityEnum.FILE_NOT_FOUND.getName());
            case FILE_INVALID_PATH_SEQUENCE:
                return new FileStorageException(ErrorCodeUtilityEnum.FILE_INVALID_PATH_SEQUENCE, ErrorCodeUtilityEnum.FILE_INVALID_PATH_SEQUENCE.getName());
            case FILE_STORE_FAILED:
                return new FileStorageException(ErrorCodeUtilityEnum.FILE_STORE_FAILED, ErrorCodeUtilityEnum.FILE_STORE_FAILED.getName());
            default:
                throw new FileStorageException(ErrorCodeUtilityEnum.FILE_INTERNAL_ERROR, ErrorCodeUtilityEnum.FILE_INTERNAL_ERROR.getName());

        }

    }
}
