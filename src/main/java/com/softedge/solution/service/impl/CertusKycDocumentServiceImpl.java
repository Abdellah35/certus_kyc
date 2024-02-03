package com.softedge.solution.service.impl;

import com.mongodb.DBObject;
import com.softedge.solution.commons.CommonUtilities;
import com.softedge.solution.contractmodels.*;
import com.softedge.solution.enuminfo.CategoryEnum;
import com.softedge.solution.enuminfo.ModuleEnum;
import com.softedge.solution.enuminfo.ProcessStatusEnum;
import com.softedge.solution.enuminfo.UserKycStatusEnum;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeCompanyEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUserEnum;
import com.softedge.solution.repomodels.KycDocumentDetails;
import com.softedge.solution.repomodels.Mail;
import com.softedge.solution.repomodels.UserKycStatusDetails;
import com.softedge.solution.repomodels.UserRegistration;
import com.softedge.solution.repository.UserRepository;
import com.softedge.solution.repository.impl.*;
import com.softedge.solution.service.CertusKycNotificationService;
import com.softedge.solution.service.certusabstractservice.CertusAbstractKycDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CertusKycDocumentServiceImpl extends CertusAbstractKycDocumentService {

    @Autowired
    private KycDocumentMetaRepositoryImpl kycDocumentMetaRepository;

    @Autowired
    private MetadataDocumentDetailsRepositoryImpl metadataDocumentDetailsRepository;

    @Autowired
    private KycDocumentDetailsReposiotoryImpl kycDocumentDetailsReposiotory;

    @Autowired
    private CompanyRepositoryImpl companyRepository;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private CertusKycNotificationService certusKycNotificationService;

    @Autowired
    private KycUserDocumentsRepositoryImpl kycUserDocumentsRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    UserRepository userRepository;

    @Value("${certus-mailbox.certus-bcc-list}")
    private String certusBccList;


    @Override
    public List<KycDocumentMetaCM> getAllKycDocumentsMeta(HttpServletRequest request) throws KycDocumentGenericModuleException {
        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration userRegistration = userRepository.findByUsername(username);
        if (userRegistration.getCategory() != null && CategoryEnum.CLIENT.getValue().equalsIgnoreCase(userRegistration.getCategory())) {
            throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
        } else {
            List<Long> documentIDs;
            documentIDs = kycUserDocumentsRepository.getAllDocumentIdsByUserId(userRegistration.getId());
            if (documentIDs.isEmpty()) {
                documentIDs.add(0l);
            }
            return kycDocumentMetaRepository.getAllDocumentsMeta(username, documentIDs);
        }
    }


    @Override
    public List<KycDocumentMetaOverviewCM> getAllKycDocumentsMetaClientOverview(HttpServletRequest request) throws KycDocumentGenericModuleException {
        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration userRegistration = userRepository.findByUsername(username);
        if (userRegistration.getCategory() != null && CategoryEnum.USER.getValue().equalsIgnoreCase(userRegistration.getCategory())) {
            throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
        } else {
            return kycDocumentMetaRepository.getAllDocumentsOverview(username);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<KycProcessDocumentDetailsCM> requestUserKycDocument(HttpServletRequest request, ClientRequestDocumentsCM clientRequestDocumentsCM) throws KycDocumentGenericModuleException, UserAccountModuleException {
        List<Long> kycIds = new ArrayList<>();
        String companyName = null;
        if (clientRequestDocumentsCM.getUserId() != null && !clientRequestDocumentsCM.getDocumentIds().isEmpty()) {
            String username = securityUtils.getUsernameFromToken(request);
            KycDocumentDetails kycDocumentDetails = new KycDocumentDetails();
            kycDocumentDetails.setProcessStatus(ProcessStatusEnum.REQUESTED.getValue());
            kycDocumentDetails.setCreatedDate(new Date());
            kycDocumentDetails.setCreatedBy(username);
            Long companyId = companyRepository.getCompanyIdByUsername(username);
            if (companyId != null) {
                kycDocumentDetails.setCompanyId(companyId);
                companyName = companyRepository.getCompanyNameByCompanyId(companyId);
            } else {
                throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
            }
            Long userId = clientRequestDocumentsCM.getUserId();
            kycDocumentDetails.setRequesteeUserId(userId);
            UserRegistration userRegistration = userRepository.findById(userId).get();
            String name = userRegistration.getName();
            String emailId = userRegistration.getUsername();
            Long requestorUserId = userRepositoryImpl.getUserIdByUsername(username);
            kycDocumentDetails.setRequestorUserId(requestorUserId);
            for (Long documentId : clientRequestDocumentsCM.getDocumentIds()) {
                kycDocumentDetails.setDocumentId(documentId);
                Long kycId = kycDocumentDetailsReposiotory.requestKycDocumentSave(kycDocumentDetails);
                kycIds.add(kycId);
            }
            if (!kycIds.isEmpty()) {
                List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMS = kycDocumentDetailsReposiotory.getKycDocumentDetailsById(kycIds);
                List<NotificationHelperCM> notificationHelperCMList = this.setNoficationHelperForKyc(kycProcessDocumentDetailsCMS, ProcessStatusEnum.REQUESTED.getValue());

                certusKycNotificationService.addKycNotification(notificationHelperCMList);
                sendEmailTrigger(emailId, companyName, name, "request_document-template", "Request for KYC Documents");
                return kycProcessDocumentDetailsCMS;
            } else {
                logger.error("Save failed...");
                throw this.errorService(ErrorCodeKYCEnum.DATA_CONNECTION_LOST.getErrorCode());
            }
        } else {
            throw this.errorService(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT.getErrorCode());
        }
    }

    public void sendEmailTrigger(String emailId, String companyName, String username, String template, String subject) {
        try {

            String mailTemplate = template;
            String email = emailId;

            logger.info("The email id of the user for whom we are send mail is -> {}", email);
            logger.info("Sending Email Template for the corporate share");

            Mail mail = new Mail();
            mail.setFrom("sales@softedgesolution.com");
            mail.setTo(emailId);
            //mail.setCc("sales@softedgesolution.com");

            String[] bccList = CommonUtilities.getStringArrayFromCommaSeparatedString(certusBccList);
            mail.setBcc(bccList);
            mail.setSubject(subject);
            Map model = new HashMap();
            model.put("name", username);
            model.put("company", companyName);
            model.put("login_url", "https://certussecure.softedgesolution.com/welcome/login");
            mail.setModel(model);
            sendSimpleMessage(mailTemplate, mail);
            logger.info("Mail sent to the user -> {}", email);

        } catch (MessagingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    private void sendSimpleMessage(String mailTemplate, Mail mail) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getModel());
        String html = templateEngine.process(mailTemplate, context);
        helper.setTo(mail.getTo());
        //helper.setCc(mail.getCc());
        helper.setBcc(mail.getBcc());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        logger.info("The mail message is -> ", message);
        emailSender.send(message);

    }

    @Override
    public DBObject getKycMetaDocumentById(Long id) {
        KycDocumentMetaCM documentsMeta
                = kycDocumentMetaRepository.getDocumentsMetaById(id);

        DBObject metadataDocument = metadataDocumentDetailsRepository.getMetadataDocumentById(id);
        metadataDocument.put("documentLogo", documentsMeta.getDocumentLogo());
        metadataDocument.put("documentName", documentsMeta.getDocumentName());
        metadataDocument.put("documentDesc", documentsMeta.getDocumentDesc());
        metadataDocument.put("documentType", documentsMeta.getDocumentType());
        metadataDocument.put("countryName", documentsMeta.getCountryName());
        metadataDocument.put("countryCode", documentsMeta.getCountryCode());

        return metadataDocument;
    }

    @Override
    public List<KycProcessDocumentDetailsCM> getAllDocumentsResquestedByUserId(HttpServletRequest request, Long userId) throws KycDocumentGenericModuleException, UserAccountModuleException {
        if (userId != null) {
            try {
                Long companyId = companyRepository.getCompanyIdByUsername(securityUtils.getUsernameFromToken(request));
                return kycDocumentDetailsReposiotory.getKycDocumentDetailsByCompanyIdAndUserId(companyId, userId);
            } catch (Exception e) {
                throw GenericExceptionHandler.exceptionHandler(e, CertusKycDocumentServiceImpl.class);
            }
        } else {
            throw this.errorService(ErrorCodeUserEnum.USER_ID_EMPTY.getErrorCode());
        }
    }

    @Override
    public ResponseEntity<?> processStatusUpdate(HttpServletRequest request, ProcessRequestCM processRequestCM) throws KycDocumentGenericModuleException, UserAccountModuleException {
        boolean status = false;
        Long companyId = companyRepository.getCompanyIdByUsername(securityUtils.getUsernameFromToken(request));
        String companyName = companyRepository.getCompanyNameByCompanyId(companyId);
        List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMSApproved = new ArrayList<>();
        List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMSRejected = new ArrayList<>();
        List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMSRequested = new ArrayList<>();
        if (processRequestCM != null && !processRequestCM.getApprovedKycIds().isEmpty()) {
            KycDocumentProcessStateCM kycDocumentProcessStateCM = new KycDocumentProcessStateCM();
            kycDocumentProcessStateCM.setProcessStatus(ProcessStatusEnum.APPROVED.getValue());
            kycDocumentProcessStateCM.setModifiedDate(new Date());
            kycDocumentProcessStateCM.setCompanyId(companyId);
            kycDocumentProcessStateCM.setModifiedBy(securityUtils.getUsernameFromToken(request));
            kycDocumentProcessStateCM.setIds(processRequestCM.getApprovedKycIds());
            Long row = kycDocumentDetailsReposiotory.updateProcessStatusDocuments(kycDocumentProcessStateCM);
            if (row > 0) {
                status = true;
            }
            kycProcessDocumentDetailsCMSApproved = kycDocumentDetailsReposiotory.getKycDocumentDetailsById(processRequestCM.getApprovedKycIds());
            if (kycProcessDocumentDetailsCMSApproved.size() > 0) {
                String name = kycProcessDocumentDetailsCMSApproved.get(0).getRequesteeUsername();
                String emailId = kycProcessDocumentDetailsCMSApproved.get(0).getRequesteeEmailId();
                sendEmailTrigger(emailId, companyName, name, "process_document-template", "KYC Documents Approved by Client");
            }
        }
        if (processRequestCM != null && !processRequestCM.getRejectedKycIds().isEmpty()) {
            KycDocumentProcessStateCM kycDocumentProcessStateCM = new KycDocumentProcessStateCM();
            kycDocumentProcessStateCM.setProcessStatus(ProcessStatusEnum.REJECTED.getValue());
            kycDocumentProcessStateCM.setModifiedDate(new Date());
            kycDocumentProcessStateCM.setCompanyId(companyId);
            kycDocumentProcessStateCM.setModifiedBy(securityUtils.getUsernameFromToken(request));
            kycDocumentProcessStateCM.setIds(processRequestCM.getRejectedKycIds());
            Long row = kycDocumentDetailsReposiotory.updateProcessStatusDocuments(kycDocumentProcessStateCM);
            if (row > 0) {
                status = true;
            }
            kycProcessDocumentDetailsCMSRejected = kycDocumentDetailsReposiotory.getKycDocumentDetailsById(processRequestCM.getRejectedKycIds());
            if (kycProcessDocumentDetailsCMSRejected.size() > 0) {
                String name = kycProcessDocumentDetailsCMSRejected.get(0).getRequesteeUsername();
                String emailId = kycProcessDocumentDetailsCMSRejected.get(0).getRequesteeEmailId();
                sendEmailTrigger(emailId, companyName, name, "process_document-template", "KYC Documents Rejected by Client");
            }
        }
        if (processRequestCM != null && !processRequestCM.getRequestKycIds().isEmpty()) {
            KycDocumentProcessStateCM kycDocumentProcessStateCM = new KycDocumentProcessStateCM();
            kycDocumentProcessStateCM.setProcessStatus(ProcessStatusEnum.REQUESTED.getValue());
            kycDocumentProcessStateCM.setModifiedDate(new Date());
            kycDocumentProcessStateCM.setCompanyId(companyId);
            kycDocumentProcessStateCM.setModifiedBy(securityUtils.getUsernameFromToken(request));
            kycDocumentProcessStateCM.setIds(processRequestCM.getRequestKycIds());
            Long row = kycDocumentDetailsReposiotory.updateProcessStatusDocuments(kycDocumentProcessStateCM);
            if (row > 0) {
                status = true;
            }
            kycProcessDocumentDetailsCMSRequested = kycDocumentDetailsReposiotory.getKycDocumentDetailsById(processRequestCM.getRequestKycIds());
            if (kycProcessDocumentDetailsCMSRequested.size() > 0) {
                String name = kycProcessDocumentDetailsCMSRequested.get(0).getRequesteeUsername();
                String emailId = kycProcessDocumentDetailsCMSRequested.get(0).getRequesteeEmailId();
                sendEmailTrigger(emailId, companyName, name, "process_document-template", "KYC Documents Requested by Client");
            }
        }
        MapperObjectCM<String, String> mapperObjectCM = new MapperObjectCM<>();
        Map<String, String> map = new HashMap<>();
        mapperObjectCM.setMapper(map);
        if (status) {
            if (!kycProcessDocumentDetailsCMSApproved.isEmpty()) {
                List<NotificationHelperCM> notificationHelperCMList = this.setNoficationHelperForKyc(kycProcessDocumentDetailsCMSApproved, ProcessStatusEnum.APPROVED.getValue().toLowerCase());
                certusKycNotificationService.addKycNotification(notificationHelperCMList);
            }
            if (!kycProcessDocumentDetailsCMSRejected.isEmpty()) {
                List<NotificationHelperCM> notificationHelperCMList = this.setNoficationHelperForKyc(kycProcessDocumentDetailsCMSRejected, ProcessStatusEnum.REJECTED.getValue().toLowerCase());
                certusKycNotificationService.addKycNotification(notificationHelperCMList);
            }
            if (!kycProcessDocumentDetailsCMSRequested.isEmpty()) {
                List<NotificationHelperCM> notificationHelperCMList = this.setNoficationHelperForKyc(kycProcessDocumentDetailsCMSRequested, ProcessStatusEnum.REQUESTED.getValue().toLowerCase());
                certusKycNotificationService.addKycNotification(notificationHelperCMList);
            }
            map.put("status", "Successfully updated");
            return new ResponseEntity(mapperObjectCM, HttpStatus.OK);
        } else {
            map.put("status", "Save Failed");
            return new ResponseEntity<>(mapperObjectCM, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<NotificationHelperCM> setNoficationHelperForKyc(List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMS, String processState) {
        List<NotificationHelperCM> notificationHelperCMList = new ArrayList<>();
        for (KycProcessDocumentDetailsCM kycProcessDocumentDetailsCM : kycProcessDocumentDetailsCMS) {
            NotificationHelperCM notificationHelperCM = new NotificationHelperCM();
            notificationHelperCM.setDocumentName(kycProcessDocumentDetailsCM.getDocumentName());
            notificationHelperCM.setCompanyName(companyRepository.getCompanyNameByCompanyId(kycProcessDocumentDetailsCM.getCompanyId()));
            notificationHelperCM.setRequesteeUserId(kycProcessDocumentDetailsCM.getRequesteeUserId());
            notificationHelperCM.setCompanyId(kycProcessDocumentDetailsCM.getCompanyId());
            notificationHelperCM.setModule(ModuleEnum.KYC_MODULE.getValue());
            notificationHelperCM.setProcessState(processState);
            //Message Text for Mobile Native
            MessageTextCM messageTextCM = new MessageTextCM();
            messageTextCM.setText(notificationHelperCM.getCompanyName());
            messageTextCM.setClassName("highlight");
            MessageTextCM messageTextCM1 = new MessageTextCM();
            messageTextCM1.setText(" has ");
            MessageTextCM messageTextCM2 = new MessageTextCM();
            messageTextCM2.setText(notificationHelperCM.getProcessState() + " ");
            MessageTextCM messageTextCM3 = new MessageTextCM();
            messageTextCM3.setText(notificationHelperCM.getDocumentName());
            List<MessageTextCM> messageTextCMS = new ArrayList<>();
            messageTextCMS.add(messageTextCM);
            messageTextCMS.add(messageTextCM1);
            messageTextCMS.add(messageTextCM2);
            messageTextCMS.add(messageTextCM3);
            //Setting native mobile notification message
            notificationHelperCM.setNativeMessage(messageTextCMS.toString());
            //Message Text to web
            String message = "<span class='highlight'>" + notificationHelperCM.getCompanyName() + "</span> has " + notificationHelperCM.getProcessState() + " " + notificationHelperCM.getDocumentName();
            notificationHelperCM.setMessage(message);

            notificationHelperCMList.add(notificationHelperCM);

        }
        return notificationHelperCMList;
    }

    @Override
    public ResponseEntity<?> deleteKycDocumentByKycId(HttpServletRequest request, Long kycId) throws KycDocumentGenericModuleException, UserAccountModuleException {
        if (kycId != null) {
            String username = securityUtils.getUsernameFromToken(request);
            Long companyId = companyRepository.getCompanyIdByUsername(username);
            List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMS = kycDocumentDetailsReposiotory.getKycDocumentDetailsById(Arrays.asList(kycId));
            if (!kycProcessDocumentDetailsCMS.isEmpty() && kycProcessDocumentDetailsCMS.get(0).getCompanyId().equals(companyId)) {
                KycProcessDocumentDetailsCM kycProcessDocumentDetailsCM = kycProcessDocumentDetailsCMS.get(0);
                String processState = kycProcessDocumentDetailsCM.getProcessStatus();
                if (processState.equalsIgnoreCase(ProcessStatusEnum.REQUESTED.getValue())) {
                    Boolean deleteStatus = kycDocumentDetailsReposiotory.deleteKycDocument(kycId, companyId);
                    if (Boolean.TRUE.equals(deleteStatus)) {
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("KycId", kycId);
                        objectMap.put("Status", "Successfull");
                        return new ResponseEntity<>(objectMap, HttpStatus.OK);
                    } else {
                        throw this.errorService(ErrorCodeKYCEnum.INTERNAL_SERVER_ERROR.getErrorCode());
                    }
                } else {
                    logger.warn("Cannot delete the KYC as it is beyond the process state Requested and current stated is  :: {}", processState);
                    throw this.errorService(ErrorCodeKYCEnum.DELETE_FAILED.getErrorCode());
                }

            } else {
                logger.warn("Empty records for KycID :: {}", kycId);
                throw this.errorService(ErrorCodeKYCEnum.EMPTY_DOCUMENTS.getErrorCode());
            }
        } else {
            logger.error("KYC ID is null and delete operation failed...");
            throw this.errorService(ErrorCodeCompanyEnum.KYC_ID_EMPTY.getErrorCode());
        }
    }

    @Override
    public ResponseEntity<UserKycStatusDetails> upsertUserKYCStatusDetails(HttpServletRequest request, Long userId, String status, boolean unregistered) throws KycDocumentGenericModuleException {
        if (userId != null) {
            String username = securityUtils.getUsernameFromToken(request);
            Long companyId = companyRepository.getCompanyIdByUsername(username);
            boolean flag = kycDocumentDetailsReposiotory.isUserkycStatusByClientExist(userId, companyId);
            UserKycStatusDetails userKycStatusDetails = new UserKycStatusDetails();
            userKycStatusDetails.setUserId(userId);
            userKycStatusDetails.setCompanyId(companyId);
            userKycStatusDetails.setStatus(status);
            userKycStatusDetails.setUnregisteredUser(unregistered);
            if (status.equalsIgnoreCase(UserKycStatusEnum.APPROVED.getValue())) {
                userKycStatusDetails.setStatus(UserKycStatusEnum.APPROVED.getValue());
            } else if (status.equalsIgnoreCase(UserKycStatusEnum.REJECTED.getValue())) {
                userKycStatusDetails.setStatus(UserKycStatusEnum.REJECTED.getValue());
            } else if (status.equalsIgnoreCase(UserKycStatusEnum.PENDING.getValue())) {
                userKycStatusDetails.setStatus(UserKycStatusEnum.PENDING.getValue());
            } else if (status.equalsIgnoreCase(UserKycStatusEnum.REQUESTED.getValue())) {
                userKycStatusDetails.setStatus(UserKycStatusEnum.REQUESTED.getValue());
            }
            if (flag) {
                //update
                userKycStatusDetails.setModifiedBy(username);
                userKycStatusDetails.setModifiedAt(new Date());
                Long id = kycDocumentDetailsReposiotory.updateUserKycStatus(userKycStatusDetails);
                if (id > 0) {
                    UserKycStatusDetails statusDetails = kycDocumentDetailsReposiotory.getUserKycStatusByClientId(userId, companyId);
                    return new ResponseEntity<UserKycStatusDetails>(statusDetails, HttpStatus.OK);
                } else {
                    throw this.errorService(ErrorCodeKYCEnum.SQL_GRAMMER_ISSUE.getErrorCode());
                }
            } else {
                //insert
                userKycStatusDetails.setCreatedBy(username);
                userKycStatusDetails.setCreatedAt(new Date());
                Long id = kycDocumentDetailsReposiotory.insertUserKycStatus(userKycStatusDetails);
                userKycStatusDetails.setId(id);
                return new ResponseEntity<UserKycStatusDetails>(userKycStatusDetails, HttpStatus.OK);
            }
        } else {
            throw this.errorService(ErrorCodeUserEnum.USER_ID_EMPTY.getErrorCode());
        }
    }

    @Override
    public ResponseEntity<UserKycStatusDetails> getUserKycStatusDetails(HttpServletRequest request, Long userId) throws KycDocumentGenericModuleException {
        if (userId != null) {
            String username = securityUtils.getUsernameFromToken(request);
            Long companyId = companyRepository.getCompanyIdByUsername(username);
            UserKycStatusDetails statusDetails = kycDocumentDetailsReposiotory.getUserKycStatusByClientId(userId, companyId);
            return new ResponseEntity<UserKycStatusDetails>(statusDetails, HttpStatus.OK);
        } else {
            throw this.errorService(ErrorCodeUserEnum.USER_ID_EMPTY.getErrorCode());
        }
    }

    public ResponseEntity<ClientDashboardUserKycStatusSummaryCM> getUserKycStatusSummary(HttpServletRequest request, Long companyId) throws KycDocumentGenericModuleException {
        String username = securityUtils.getUsernameFromToken(request);
        boolean isAdmin = Boolean.FALSE.booleanValue();
        UserRegistration userRegistration = userRepository.findByUsername(username);
        if ("admin".equalsIgnoreCase(userRegistration.getCategory())) {
            isAdmin = Boolean.TRUE.booleanValue();
        } else if (companyId == null) {
            companyId = companyRepository.getCompanyIdByUsername(username);
        }
        ClientDashboardUserKycStatusSummaryCM clientDashboardUserKycStatusSummaryCM = kycDocumentDetailsReposiotory.getClientDashboardUserCounts(companyId, isAdmin);
        return new ResponseEntity<>(clientDashboardUserKycStatusSummaryCM, HttpStatus.OK);
    }


}
