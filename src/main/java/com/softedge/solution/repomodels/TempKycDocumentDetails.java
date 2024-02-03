package com.softedge.solution.repomodels;

import lombok.Data;

import java.util.Date;

@Data
public class TempKycDocumentDetails {
    private Long id;
    private Long companyId;
    private Long requestorUserId;
    private Long requesteeUserId;
    private Long documentId;
    private Date createdAt;
    private String createdBy;

}
