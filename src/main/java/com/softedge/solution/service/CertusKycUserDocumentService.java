package com.softedge.solution.service;

import com.mongodb.DBObject;
import com.softedge.solution.contractmodels.KycProcessDocumentDetailsCM;
import com.softedge.solution.contractmodels.RequestedCompaniesCM;
import com.softedge.solution.contractmodels.UserSubmittedKycDocumentsCM;
import com.softedge.solution.exceptionhandlers.custom.company.CompanyServiceModuleException;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.repomodels.UserKycStatusDetails;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertusKycUserDocumentService {

    ResponseEntity<?> uploadAttachmentImage(MultipartFile file);

    DBObject addUserDocument(HttpServletRequest request, Document document);

    DBObject updateUserDocument(HttpServletRequest request,Document document);

    DBObject getUserDocument(HttpServletRequest request, Integer docId, Long userId);

    String deleteUserDocument(HttpServletRequest request, Integer docId, Long userId);

    List<KycProcessDocumentDetailsCM> getAllDocumentsResquestedByCompanyId(HttpServletRequest request, Long companyId)throws KycDocumentGenericModuleException, UserAccountModuleException, CompanyServiceModuleException;

    ResponseEntity<?> updateSubmittedDocumentsByUser(HttpServletRequest request, UserSubmittedKycDocumentsCM userSubmittedKycDocumentsCM) throws KycDocumentGenericModuleException, UserAccountModuleException, CompanyServiceModuleException;

    ResponseEntity<?> getAllDocumentUser(HttpServletRequest request) throws KycDocumentGenericModuleException, UserAccountModuleException, CompanyServiceModuleException;

    List<RequestedCompaniesCM> getRequestedCompanies(HttpServletRequest request) throws KycDocumentGenericModuleException;

    ResponseEntity<UserKycStatusDetails> getUserKycStatusDetails(HttpServletRequest request, Long companyId)throws KycDocumentGenericModuleException;



}
