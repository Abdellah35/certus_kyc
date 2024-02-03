package com.softedge.solution.contractmodels;

import lombok.Data;

@Data
public class NotificationHelperCM {

    private String documentName;
    private Long companyId;
    private String nativeMessage;
    private String message;
    private String companyName;
    private Long requesteeUserId;
    private Long requestorUserId;
    private String module;
    private String processState;
}
