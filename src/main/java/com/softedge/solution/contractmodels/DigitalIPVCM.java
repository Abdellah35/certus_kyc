package com.softedge.solution.contractmodels;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DigitalIPVCM {

    private String verificationStatus;
    private String userIPVImage;
    private Integer ipvCode;
    private Date userRequestRaisedTime;
    private Date userRequestModifiedTime;
    private Date auditVerifiedTime;
}
