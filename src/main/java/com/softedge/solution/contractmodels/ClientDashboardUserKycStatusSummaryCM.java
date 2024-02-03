package com.softedge.solution.contractmodels;

import lombok.Data;

@Data
public class ClientDashboardUserKycStatusSummaryCM {

    private Long requested;
    private Long approved;
    private Long pending;
    private Long rejected;
}
