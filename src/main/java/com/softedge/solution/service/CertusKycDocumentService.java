package com.softedge.solution.service;

import com.mongodb.DBObject;
import com.softedge.solution.contractmodels.*;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.repomodels.UserKycStatusDetails;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertusKycDocumentService {

    List<KycDocumentMetaCM> getAllKycDocumentsMeta(HttpServletRequest request) throws Exception;

    List<KycDocumentMetaOverviewCM> getAllKycDocumentsMetaClientOverview(HttpServletRequest request) throws KycDocumentGenericModuleException;

    DBObject getKycMetaDocumentById(Long id);

    List<KycProcessDocumentDetailsCM> requestUserKycDocument(HttpServletRequest request, ClientRequestDocumentsCM clientRequestDocumentsCM) throws KycDocumentGenericModuleException;

    List<KycProcessDocumentDetailsCM> getAllDocumentsResquestedByUserId(HttpServletRequest request, Long userId)throws KycDocumentGenericModuleException, UserAccountModuleException;

    ResponseEntity<?> processStatusUpdate(HttpServletRequest request, ProcessRequestCM processRequestCM) throws KycDocumentGenericModuleException, UserAccountModuleException;

    ResponseEntity<?> deleteKycDocumentByKycId(HttpServletRequest request, Long kycId)throws KycDocumentGenericModuleException, UserAccountModuleException;

    ResponseEntity<UserKycStatusDetails> upsertUserKYCStatusDetails(HttpServletRequest request, Long userId, String status, boolean unregistered)throws KycDocumentGenericModuleException;

    ResponseEntity<UserKycStatusDetails> getUserKycStatusDetails(HttpServletRequest request, Long userId)throws KycDocumentGenericModuleException;

    ResponseEntity<ClientDashboardUserKycStatusSummaryCM> getUserKycStatusSummary(HttpServletRequest request, Long companyId)throws KycDocumentGenericModuleException;
}
