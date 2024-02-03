package com.softedge.solution.contractmodels;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class KycDocumentProcessStateCM {

    private List<Long> ids;
    private Date modifiedDate;
    private String modifiedBy;
    private Long companyId;
    private String processStatus;

}
