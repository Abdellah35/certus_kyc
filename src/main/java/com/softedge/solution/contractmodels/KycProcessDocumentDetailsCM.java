package com.softedge.solution.contractmodels;

import lombok.Data;

import java.util.Date;

@Data
public class KycProcessDocumentDetailsCM {

    private Long id;
    private Long documentId;
    private String documentLogo;
    private String documentName;
    private String documentType;
    private String documentDesc;
    private String countryCode;
    private String countryName;
    private String createdBy;
    private Date createdDate;
    private Date modifiedDate;
    private String modifiedBy;
    private Long requestorUserId;
    private Long requesteeUserId;
    private String processStatus;
    private Long companyId;
    private boolean comments;
    private String requestorUsername;
    private String requesteeUsername;
    private String requesteeEmailId;

}
