package com.softedge.solution.service;

import com.softedge.solution.contractmodels.KycDocumentProcessStateCM;
import com.softedge.solution.contractmodels.NotificationHelperCM;

import java.util.List;


public interface CertusKycNotificationService {

    void addKycNotification(NotificationHelperCM NotificationHelperCM);

    void addKycNotification(List<NotificationHelperCM> notificationHelperCMS);
}
