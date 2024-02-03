package com.softedge.solution.service.impl;

import com.softedge.solution.commons.CommonUtilities;
import com.softedge.solution.contractmodels.*;
import com.softedge.solution.enuminfo.CategoryEnum;
import com.softedge.solution.enuminfo.ProcessStatusEnum;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUserEnum;
import com.softedge.solution.repomodels.Mail;
import com.softedge.solution.repomodels.TempKycDocumentDetails;
import com.softedge.solution.repomodels.TempUser;
import com.softedge.solution.repomodels.UserRegistration;
import com.softedge.solution.repository.UserRepository;
import com.softedge.solution.repository.impl.CompanyRepositoryImpl;
import com.softedge.solution.repository.impl.TempKycDocumentDetailsRepositoryImpl;
import com.softedge.solution.repository.impl.TempUserRepositoryImpl;
import com.softedge.solution.repository.impl.UserRepositoryImpl;
import com.softedge.solution.security.util.SecurityUtils;
import com.softedge.solution.service.CertusKycNotificationService;
import com.softedge.solution.service.CertusTempKycDocumentService;
import com.softedge.solution.service.certusabstractservice.CerterGenericErrorHandlingService;
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
public class CertusTempKycDocumentServiceImpl extends CerterGenericErrorHandlingService implements CertusTempKycDocumentService {

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected UserRepository userRepository;



    @Autowired
    CompanyRepositoryImpl companyRepository;

    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Autowired
    TempKycDocumentDetailsRepositoryImpl tempKycDocumentDetailsRepository;

    @Autowired
    private CertusKycNotificationService certusKycNotificationService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private TempUserRepositoryImpl tempUserRepository;

    @Value("${certus-mailbox.certus-bcc-list}")
    private String certusBccList;



    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<KycProcessDocumentDetailsCM> requestUserTempKycDocument(HttpServletRequest request, ClientRequestDocumentsCM clientRequestDocumentsCM) throws KycDocumentGenericModuleException, UserAccountModuleException {
        List<Long> kycIds = new ArrayList<>();
        String username = securityUtils.getUsernameFromToken(request);
        UserRegistration userRegistration = userRepository.findByUsername(username);
        if (userRegistration.getCategory().equalsIgnoreCase(CategoryEnum.CLIENT.getValue())) {
            if (clientRequestDocumentsCM.getUserId() != null) {
                if (clientRequestDocumentsCM.getDocumentIds().size() > 0) {

                    TempKycDocumentDetails kycDocumentDetails = new TempKycDocumentDetails();
                    String companyName = null;
                    kycDocumentDetails.setCreatedAt(new Date());
                    kycDocumentDetails.setCreatedBy(username);
                    Long companyId = companyRepository.getCompanyIdByUsername(username);

                    if (companyId != null) {
                        kycDocumentDetails.setCompanyId(companyId);
                        companyName = companyRepository.getCompanyNameByCompanyId(companyId);
                    } else {
                        throw this.errorService(ErrorCodeKYCEnum.UNAUTHORIZED.getErrorCode());
                    }
                    Long requesteeUserId = clientRequestDocumentsCM.getUserId();
                    kycDocumentDetails.setRequesteeUserId(requesteeUserId);
                    TempUser tempUser = tempUserRepository.getTempUserById(requesteeUserId);
                    String requesteeEmailId = tempUser.getEmailId();
                    Long requestorUserId = userRepositoryImpl.getUserIdByUsername(username);
                    kycDocumentDetails.setRequestorUserId(requestorUserId);
                    for (Long documentId : clientRequestDocumentsCM.getDocumentIds()) {
                        kycDocumentDetails.setDocumentId(documentId);
                        Long kycId = tempKycDocumentDetailsRepository.requestTempKycDocumentSave(kycDocumentDetails);
                        kycIds.add(kycId);
                    }
                    if (kycIds.size() > 0) {
                        List<KycProcessDocumentDetailsCM> kycProcessDocumentDetailsCMS = tempKycDocumentDetailsRepository.getTempKycDocumentDetailsById(kycIds);
                        sendEmailTrigger(requesteeEmailId, companyName, tempUser.getName());
                        return kycProcessDocumentDetailsCMS;
                    } else {
                        logger.error("Save failed...");
                        throw this.errorService(ErrorCodeKYCEnum.DATA_CONNECTION_LOST.getErrorCode());
                    }
                } else {
                    throw this.errorService(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT.getErrorCode());
                }
            } else {
                throw this.errorService(ErrorCodeUserEnum.USER_ID_EMPTY.getErrorCode());
            }
        }
        else{
            throw this.errorService(ErrorCodeUserEnum.UNAUTHORIZED.getErrorCode());
        }

    }


    @Override
    public List<KycProcessDocumentDetailsCM> getAllTempDocumentsResquestedByUserId(HttpServletRequest request, Long userId) throws KycDocumentGenericModuleException, UserAccountModuleException {
        if (userId != null) {
            try {
                Long companyId = companyRepository.getCompanyIdByUsername(securityUtils.getUsernameFromToken(request));
                return tempKycDocumentDetailsRepository.getTempKycDocumentDetailsByCompanyIdAndUserId(companyId, userId);
            } catch (Exception e) {
                throw GenericExceptionHandler.exceptionHandler(e, CertusKycDocumentServiceImpl.class);
            }
        } else {
            throw this.errorService(ErrorCodeUserEnum.USER_ID_EMPTY.getErrorCode());
        }
    }


    public void sendEmailTrigger(String emailId,  String companyName, String username){
        try {

            String mailTemplate = "register-email-template";
            String email = emailId;

            logger.info("The email id of the user for whom we are send mail is -> {}", email);
            logger.info("Sending Email Template for the corporate share");

            Mail mail = new Mail();
            mail.setFrom("sales@softedgesolution.com");
            mail.setTo(emailId);
            //mail.setCc("sales@softedgesolution.com");

            String[] bccList = CommonUtilities.getStringArrayFromCommaSeparatedString(certusBccList);
            mail.setBcc(bccList);
            mail.setSubject("Request to register for Certus to submit KYC Documents");
            Map model = new HashMap();
            model.put("username",username);
            model.put("company", companyName);
            model.put("otp_registration_url", "https://certussecure.softedgesolution.com/welcome/registor");
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
}
