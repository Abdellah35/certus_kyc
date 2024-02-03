package com.softedge.solution.repomodels;

import lombok.Data;

import java.util.Date;

@Data
public class UserKycStatusDetails {

    private Long id;
    private Long userId;
    private Long companyId;
    private String createdBy;
    private Date createdAt;
    private String modifiedBy;
    private Date modifiedAt;
    private String status;
    private boolean unregisteredUser;
}
