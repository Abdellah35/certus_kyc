package com.softedge.solution.service.impl;

import com.mongodb.DBObject;
import com.softedge.solution.commons.CommonUtilities;
import com.softedge.solution.contractmodels.*;
import com.softedge.solution.enuminfo.CategoryEnum;
import com.softedge.solution.enuminfo.ModuleEnum;
import com.softedge.solution.enuminfo.ProcessStatusEnum;
import com.softedge.solution.enuminfo.UserKycStatusEnum;
import com.softedge.solution.exceptionhandlers.BaseException;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.company.CompanyServiceModuleException;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeCompanyEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUserEnum;
import com.softedge.solution.repomodels.Mail;
import com.softedge.solution.repomodels.UserKycStatusDetails;
import com.softedge.solution.repomodels.UserRegistration;
import com.softedge.solution.repository.UserRepository;
import com.softedge.solution.repository.impl.CompanyRepositoryImpl;
import com.softedge.solution.repository.impl.KycDocumentDetailsReposiotoryImpl;
import com.softedge.solution.repository.impl.KycUserDocumentsRepositoryImpl;
import com.softedge.solution.repository.impl.UserRepositoryImpl;
import com.softedge.solution.security.util.SecurityUtils;
import com.softedge.solution.service.CertusKycNotificationService;
import com.softedge.solution.service.CertusKycUserDocumentService;
import com.softedge.solution.service.certusabstractservice.CerterGenericErrorHandlingService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CertusKycUserDocumentServiceImpl extends CerterGenericErrorHandlingService<BaseException> implements CertusKycUserDocumentService {

    @Autowired
    protected SecurityUtils securityUtils;
    @Autowired
    protected UserRepository userRepository;
    Path fileStorageLocation;
    @Value("${user.attachdir}")
    private String attachdir;
    @Value("${user.nginx.dir}")
    private String userNginxDir;
    @Value("${base.url}")
    private String baseUrl;
    @Autowired
    private KycUserDocumentsRepositoryImpl kycUserDocumentsRepository;
    @Autowired
    private KycDocumentDetailsReposiotoryImpl kycDocumentDetailsReposiotory;
    @Autowired
    private CompanyRepositoryImpl companyRepository;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private CertusKycNotificationService certusKycNotificationService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${certus-mailbox.certus-bcc-list}")
    private String certusBccList;

    @PostConstruct
    public void init() {

        fileStorageLocation = Paths.get(userNginxDir).toAbsolutePath().normalize();

    }


    @Override
    public ResponseEntity<?> uploadAttachmentImage(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        logger.info("Orginal file name {} ", fileName);
        String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        fileName = currentDate + "-" + fileName;

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                MapperObjectCM<String, String>  mapperObjectCM = new MapperObjectCM<String, String> ();
                Map<String, String> map = new HashMap<>();
                map.put("Status","Sorry! Filename contains invalid path sequence "+fileName);
                mapperObjectCM.setMapper(map);
                return new ResponseEntity<MapperObjectCM<String, String>>(mapperObjectCM, HttpStatus.BAD_REQUEST);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(fileName);
            logger.info("Target Location {} ", targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            logger.info("base path {} ", baseUrl);

            String fileDownloadUri = ServletUriComponentsBuilder.
                    fromPath(baseUrl)
                    .scheme("https")
                    .path(attachdir)
                    .path(fileName)
                    .toUriString();

            fileDownloadUri=fileDownloadUri.replace("http:/","");

            logger.info("fileDownloadUri {} ", fileDownloadUri);

            MapperObjectCM<String, String> mapperObjectCM = new MapperObjectCM<>();
            Map<String, String> map = new HashMap<>();
            mapperObjectCM.setMapper(map);
            map.put("attachment-url", fileDownloadUri);
            map.put("filename", fileName);
            return new ResponseEntity<MapperObjectCM<String, String>>(mapperObjectCM, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not store file " + fileName + ". Please try again!", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @Override
    public DBObject addUserDocument(HttpServletRequest request, Document document) {
        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration user = userRepository.findByUsername(username);
        if (user.getCategory() != null && CategoryEnum.CLIENT.getValue().equalsIgnoreCase(user.getCategory())) {
            throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
        }
        else{
            logger.info("Username is {}, user id is {} ", username, user.getId());
            Long userId = user.getId();
            Integer docId = (Integer) document.get("docId");
            DBObject userDocument = kycUserDocumentsRepository.getUserDocument(userId, docId);
            document.put("userId", userId);
            document.put("lastModifiedTime", new Date());
            if (userDocument == null) {
                kycUserDocumentsRepository.loadUserDocument(document);
            } else {
                kycUserDocumentsRepository.updateUserDocument(document, userId, docId);
            }
            return kycUserDocumentsRepository.getUserDocument(userId, docId);
        }

    }

    @Override
    public DBObject updateUserDocument(HttpServletRequest request, Document document) {
        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration user = userRepository.findByUsername(username);
        if (user.getCategory() != null && CategoryEnum.CLIENT.getValue().equalsIgnoreCase(user.getCategory())) {
            throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
        }else{
            logger.info("Username is {}, user id is {} ", username, user.getId());
            Long userId = user.getId();
            Integer docId = (Integer) document.get("docId");
            DBObject userDocument = kycUserDocumentsRepository.getUserDocument(userId, docId);
            document.put("userId", userId);
            document.put("lastModifiedTime", new Date());
            if (userDocument == null) {
                kycUserDocumentsRepository.loadUserDocument(document);
            } else {
                kycUserDocumentsRepository.updateUserDocument(document, userId, docId);
            }
            return kycUserDocumentsRepository.getUserDocument(userId, docId);
        }

    }

    @Override
    public DBObject getUserDocument(HttpServletRequest request, Integer docId, Long userId) throws KycDocumentGenericModuleException {

        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration user = userRepository.findByUsername(username);

        if (userId == null) {
            if(user.getCategory() != null  && CategoryEnum.CLIENT.getValue().equalsIgnoreCase(user.getCategory())){
                throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
            }
            userId = user.getId();
            logger.info("Username is {}, user id is {} ", username, user.getId());
        }
        if(user!=null && CategoryEnum.CLIENT.getValue().equalsIgnoreCase(user.getCategory())){
            Long companyId = companyRepository.getCompanyIdByUsername(username);
            if(!kycDocumentDetailsReposiotory.getUserDocumentRequestedByClient(userId, companyId, (long)docId)){
                throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
            }
        }
        DBObject userDocument = kycUserDocumentsRepository.getUserDocument(userId, docId);
        if (userDocument != null) {
            return userDocument;
        } else {
            throw new CerterGenericErrorHandlingService<KycDocumentGenericModuleException>().errorService(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT.getErrorCode());

        }

    }

    @Override
    public String deleteUserDocument(HttpServletRequest request, Integer docId, Long userId) throws KycDocumentGenericModuleException {

        if (userId == null) {
            String username = this.securityUtils.getUsernameFromToken(request);
            UserRegistration user = userRepository.findByUsername(username);
            userId = user.getId();
            logger.info("Username is {}, user id is {} ", username, user.getId());
        }
        boolean deleted = kycUserDocumentsRepository.deleteUserDocument(userId, docId);
        if (deleted) {
            return "user document of userId "+userId+" docId"+docId+" is deleted";

        } else {
            throw new CerterGenericErrorHandlingService<KycDocumentGenericModuleException>().errorService(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT.getErrorCode());
        }

    }

    @Override
    public List<KycProcessDocumentDetailsCM> getAllDocumentsResquestedByCompanyId(HttpServletRequest request, Long companyId) throws KycDocumentGenericModuleException, UserAccountModuleException, CompanyServiceModuleException {
        if (companyId != null) {
            try {
                return kycDocumentDetailsReposiotory.getKycDocumentDetailsByCompanyIdAndUserId(companyId, userRepositoryImpl.getUserIdByUsername(securityUtils.getUsernameFromToken(request)));
            } catch (Exception e) {
                throw GenericExceptionHandler.exceptionHandler(e, CertusKycDocumentServiceImpl.class);
            }
        } else {
            throw this.errorService(ErrorCodeCompanyEnum.COMPANY_ID_INVALID.getErrorCode());
        }
    }

    @Override
    public ResponseEntity<?> updateSubmittedDocumentsByUser(HttpServletRequest request, UserSubmittedKycDocumentsCM userSubmittedKycDocumentsCM) throws KycDocumentGenericModuleException, UserAccountModuleException, CompanyServiceModuleException {
        if (userSubmittedKycDocumentsCM != null) {
            String username = securityUtils.getUsernameFromToken(request);
            if (userSubmittedKycDocumentsCM.getCompanyId() != null) {
                if (userSubmittedKycDocumentsCM.getKycIds().size() > 0) {
                    KycDocumentProcessStateCM kycDocumentProcessStateCM = new KycDocumentProcessStateCM();
                    kycDocumentProcessStateCM.setIds(userSubmittedKycDocumentsCM.getKycIds());
                    kycDocumentProcessStateCM.setModifiedBy(username);
                    kycDocumentProcessStateCM.setModifiedDate(new Date());
                    kycDocumentProcessStateCM.setProcessStatus(ProcessStatusEnum.PENDING.getValue());
                    kycDocumentProcessStateCM.setCompanyId(userSubmittedKycDocumentsCM.getCompanyId());
                    Long status = kycDocumentDetailsReposiotory.updateProcessStatusDocuments(kycDocumentProcessStateCM);
                    if (status > 0) {
                        List<NotificationHelperCM> notificationHelperCMList = this.setSubmitKycNotification(userSubmittedKycDocumentsCM.getKycIds(), userRepositoryImpl.getNameByEmailId(username));
                        certusKycNotificationService.addKycNotification(notificationHelperCMList);
                        Long companyId = userSubmittedKycDocumentsCM.getCompanyId();
                        String companyName = companyRepository.getCompanyNameByCompanyId(companyId);
                        MapperObjectCM<String, String> mapperObjectCM = new MapperObjectCM<>();
                        Map<String, String> map = new HashMap<>();
                        map.put("Status", "Saved Successfully");
                        mapperObjectCM.setMapper(map);
                        //Change KYC Status
                        Long userId = userRepositoryImpl.getUserIdByUsername(username);
                        changeKYCStatus(userId, kycDocumentProcessStateCM.getCompanyId());
                        String name = userRepositoryImpl.getNameByEmailId(username);
                        sendEmailTrigger(username,  companyName, name, "submit_user_document-template","Submission of user documents successful");
                        return new ResponseEntity<MapperObjectCM<String, String>>(mapperObjectCM, HttpStatus.OK);
                    } else {
                        throw this.errorService(ErrorCodeKYCEnum.DATA_INCORRECT.getErrorCode());
                    }
                } else {
                    throw this.errorService(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT.getErrorCode());
                }
            } else {
                throw this.errorService(ErrorCodeCompanyEnum.COMPANY_ID_INVALID.getErrorCode());
            }
        } else {
            throw this.errorService(ErrorCodeUserEnum.INVALID_FORM.getErrorCode());
        }
    }

    public void sendEmailTrigger(String emailId, String companyName, String name, String template, String subject){
        try {

            String mailTemplate = template;
            String email = emailId;

            logger.info("The email id of the user for whom we are send mail is -> {}", email);
            logger.info("Sending Email Template for the corporate share");

            Mail mail = new Mail();
            mail.setFrom("sales@softedgesolution.com");
            mail.setTo(emailId);
            String[] bccList = CommonUtilities.getStringArrayFromCommaSeparatedString(certusBccList);
            mail.setBcc(bccList);
            mail.setSubject(subject);
            Map model = new HashMap();
            model.put("name",name);
            model.put("company", companyName);
            mail.setModel(model);
            sendSimpleMessage(mailTemplate,mail);
            logger.info("Mail sent to the user -> {}",email);

        }catch (MessagingException e){
            logger.error(e.getMessage());
        }catch (IOException e){
            logger.error(e.getMessage());
        }catch (Exception e){
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
        logger.info("The mail message is -> ",message);
        emailSender.send(message);

    }


    private List<NotificationHelperCM> setSubmitKycNotification(List<Long> kycIds, String username) {
        List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMS = kycDocumentDetailsReposiotory.getKycDocumentDetailsById(kycIds);
        List<NotificationHelperCM> notificationHelperCMList = new ArrayList<>();
        for (KycProcessDocumentDetailsCM kycProcessDocumentDetailsCM : kycProcessDocumentDetailsCMS) {
            NotificationHelperCM notificationHelperCM = new NotificationHelperCM();
            notificationHelperCM.setProcessState(ProcessStatusEnum.SUBMITTED.getValue());
            notificationHelperCM.setCompanyId(kycProcessDocumentDetailsCM.getCompanyId());
            notificationHelperCM.setModule(ModuleEnum.KYC_MODULE.getValue());
            notificationHelperCM.setRequestorUserId(kycProcessDocumentDetailsCM.getRequestorUserId());
            notificationHelperCM.setCompanyName(companyRepository.getCompanyNameByCompanyId(kycProcessDocumentDetailsCM.getCompanyId()));
            notificationHelperCM.setDocumentName(kycProcessDocumentDetailsCM.getDocumentName());
            //Message Text for Mobile Native
            MessageTextCM messageTextCM = new MessageTextCM();
            messageTextCM.setText(username);
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
            String message = "<span class='highlight'>" + username + "</span> has " + notificationHelperCM.getProcessState() + " " + notificationHelperCM.getDocumentName();
            notificationHelperCM.setMessage(message);
            notificationHelperCMList.add(notificationHelperCM);
        }
        return notificationHelperCMList;
    }


    @Override
    public ResponseEntity<?> getAllDocumentUser(HttpServletRequest request) throws KycDocumentGenericModuleException, UserAccountModuleException, CompanyServiceModuleException {
        try {
            List<UploadedDocumentsCM> uploadedDocumentsCMS = new ArrayList<>();
            String username = this.securityUtils.getUsernameFromToken(request);
            UserRegistration user = userRepository.findByUsername(username);
            Long userId = user.getId();
            List<Long> documentIDs = kycUserDocumentsRepository.getAllDocumentIdsByUserId(userId);
            logger.warn("The document ids : {}", documentIDs);
            if (documentIDs.size() > 0) {
                uploadedDocumentsCMS = kycDocumentDetailsReposiotory.getKycDocumentDetailsByUserId(userId, documentIDs);
            }
            return new ResponseEntity<>(uploadedDocumentsCMS, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error is {}", e);
            throw this.errorService(ErrorCodeUserEnum.INVALID_FORM.getErrorCode());
        }

    }

    @Override
    public List<RequestedCompaniesCM> getRequestedCompanies(HttpServletRequest request) throws KycDocumentGenericModuleException {
        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration user = userRepository.findByUsername(username);
        Long userId = user.getId();
        List<RequestedCompaniesCM> requestedCompaniesCMS = kycDocumentDetailsReposiotory.getRequestedCompanies(userId);
        return requestedCompaniesCMS;
    }

    private void changeKYCStatus(Long userId, Long companyId){
        UserKycStatusDetails userKycStatusDetails = kycDocumentDetailsReposiotory.getUserKycStatusByClientId(userId, companyId);
        if(StringUtils.isEmpty(userKycStatusDetails.getStatus()) || userKycStatusDetails.getStatus().equalsIgnoreCase(UserKycStatusEnum.REQUESTED.getValue())) {
            userKycStatusDetails.setModifiedBy("system");
            userKycStatusDetails.setModifiedAt(new Date());
            userKycStatusDetails.setStatus(UserKycStatusEnum.PENDING.getValue());
            Long id = kycDocumentDetailsReposiotory.updateUserKycStatus(userKycStatusDetails);
            if(id>0){
                logger.info("Success");
            }else{
                logger.info("UnSuccessfull");
            }
        }
    }

    @Override
    public ResponseEntity<UserKycStatusDetails> getUserKycStatusDetails(HttpServletRequest request, Long companyId)throws KycDocumentGenericModuleException{
        if(companyId != null){
            String username = securityUtils.getUsernameFromToken(request);
            Long userId =  userRepositoryImpl.getUserIdByUsername(username);
            UserKycStatusDetails statusDetails = kycDocumentDetailsReposiotory.getUserKycStatusByClientId(userId, companyId);
            return new ResponseEntity<UserKycStatusDetails>(statusDetails, HttpStatus.OK);
        }else{
            throw this.errorService(ErrorCodeCompanyEnum.CATEGORY_MANDATORY.getErrorCode());
        }
    }
}
