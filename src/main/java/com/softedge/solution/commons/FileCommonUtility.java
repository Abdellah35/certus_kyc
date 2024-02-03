package com.softedge.solution.commons;

import com.softedge.solution.exceptionhandlers.custom.Utilities.FileStorageException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUtilityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileCommonUtility {

    private final static Logger logger = LoggerFactory.getLogger(FileCommonUtility.class);

    public static String storeFile(MultipartFile file, Path fileStorageLocation) {
        // Normalize file name
        String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        fileName = currentDate + "-" + fileName;

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException(ErrorCodeUtilityEnum.FILE_INVALID_PATH_SEQUENCE);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            logger.error("Error -> {}", ex);
            throw new FileStorageException(ErrorCodeUtilityEnum.FILE_STORE_FAILED);
        }
    }

    public static Resource loadFileAsResource(String fileName, Path fileStorageLocation) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException(ErrorCodeUtilityEnum.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            logger.error("Error -> {}", ex);
            throw new FileStorageException(ErrorCodeUtilityEnum.FILE_NOT_FOUND);
        }
    }

    public static void removeFileResource(String fileLocation) {
        try {
            File file = new File(fileLocation);
            String path = file.getCanonicalPath();
            path = path.replaceAll("\\/", "\\"); //For the linux systems (Find the better way to approach the issue)
//            String[] arrayPath = path.split("\\/ \\.");
//            String fullPath = arrayPath[0]+arrayPath[1];
            file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            //Do nothing
            logger.error("Error -> {}", e);
        }
    }
}
