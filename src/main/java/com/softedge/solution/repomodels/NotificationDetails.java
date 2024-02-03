package com.softedge.solution.repomodels;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDetails {

    private Long id;
    private String message;
    private String nativeMessage;
    private String module;
    private Date createdAt;
    private Long requesteeUserId;
    private Long requestorUserId;
    private Long companyId;
    private boolean messageRead = false;


}
