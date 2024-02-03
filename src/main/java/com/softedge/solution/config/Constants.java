package com.softedge.solution.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_TESTING = "testing";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_SWAGGER = "swagger";

    public static final String SYSTEM_ACCOUNT = "system";
    public static String UPLOAD_DOCUMENT_ATTACHMENTS;
    public static String UPLOAD_IPV_IMAGE_URI;

    private Constants() {
    }

    @Value("${document.attachments}")
    public static void setUploadDocumentAttachments(String uploadDocumentAttachments) {
        UPLOAD_DOCUMENT_ATTACHMENTS = uploadDocumentAttachments;
    }

    @Value("${digitalipv.imagedir}")
    public static void setUploadIpvImageUri(String uploadIpvImageUri) {
        UPLOAD_IPV_IMAGE_URI = uploadIpvImageUri;
    }
}
