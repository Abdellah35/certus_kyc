package com.softedge.solution.contractmodels;

import lombok.Data;

import java.util.Date;

@Data
public class RequestedCompaniesCM {

    private Long companyId;
    private String companyName;
    private String logo;
    private String notificationCount;
    private Date lastRequestedDate;
    private String kycStatus;

}
