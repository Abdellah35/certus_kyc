package com.softedge.solution.service.impl;

import com.softedge.solution.contractmodels.MessageTextCM;
import com.softedge.solution.contractmodels.NotificationHelperCM;
import com.softedge.solution.enuminfo.ModuleEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.repomodels.NotificationDetails;
import com.softedge.solution.repository.impl.NotificationRepositoryImpl;
import com.softedge.solution.service.CertusKycNotificationService;
import com.softedge.solution.service.certusabstractservice.CerterGenericErrorHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertusKycNotificationServiceImpl extends CerterGenericErrorHandlingService implements CertusKycNotificationService {

    @Autowired
    NotificationRepositoryImpl notificationRepository;

    @Override
    public void addKycNotification(NotificationHelperCM notificationHelperCM) {
        long key = 0;
        NotificationDetails notificationDetails = new NotificationDetails();
        notificationDetails.setCompanyId(notificationHelperCM.getCompanyId());
        notificationDetails.setCreatedAt(new Date());
        notificationDetails.setNativeMessage(notificationHelperCM.getNativeMessage());
        notificationDetails.setMessage(notificationHelperCM.getMessage());
        notificationDetails.setMessageRead(false);
        notificationDetails.setRequesteeUserId(notificationHelperCM.getRequesteeUserId());
        notificationDetails.setModule(ModuleEnum.KYC_MODULE.getValue());
        notificationDetails.setRequestorUserId(notificationHelperCM.getRequestorUserId());
        try {
             key = notificationRepository.notificationKycDocumentSave(notificationDetails);
        }catch (Exception e){
            throw this.errorService(ErrorCodeKYCEnum.NOTIFICATION_FAILUER.getErrorCode());
        }
        if(key>0){
            logger.info("Successfully saved notification :: {}", notificationDetails.toString());
        }else{
            logger.info("Failed to saved notification :: {}", notificationDetails.toString());
        }

    }

    @Override
    public void addKycNotification(List<NotificationHelperCM> notificationHelperCMS) {
        long key = 0;
        for(NotificationHelperCM notificationHelperCM : notificationHelperCMS) {
            NotificationDetails notificationDetails = new NotificationDetails();
            notificationDetails.setCompanyId(notificationHelperCM.getCompanyId());
            notificationDetails.setCreatedAt(new Date());
            notificationDetails.setNativeMessage(notificationHelperCM.getNativeMessage());
            notificationDetails.setMessage(notificationHelperCM.getMessage());
            notificationDetails.setMessageRead(false);
            notificationDetails.setRequesteeUserId(notificationHelperCM.getRequesteeUserId());
            notificationDetails.setModule(ModuleEnum.KYC_MODULE.getValue());
            notificationDetails.setRequestorUserId(notificationHelperCM.getRequestorUserId());
            try {
                key = notificationRepository.notificationKycDocumentSave(notificationDetails);
            } catch (Exception e) {
                throw this.errorService(ErrorCodeKYCEnum.NOTIFICATION_FAILUER.getErrorCode());
            }
            if (key > 0) {
                logger.info("Successfully saved notification :: {}", notificationDetails.toString());
            } else {
                logger.info("Failed to saved notification :: {}", notificationDetails.toString());
            }
        }

    }
}
