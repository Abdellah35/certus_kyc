package com.softedge.solution.contractmodels;

import lombok.Data;

import java.util.List;

@Data
public class UserSubmittedKycDocumentsCM{

    private Long companyId;
    private List<Long> kycIds;


}
