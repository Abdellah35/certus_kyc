package com.softedge.solution.repomodels;

import lombok.Data;

import java.util.Date;

@Data
public class KycDocumentDetails {
    private Long id;
    private Long companyId;
    private Long requestorUserId;
    private Long requesteeUserId;
    private Long documentId;
    private String processStatus;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
}
