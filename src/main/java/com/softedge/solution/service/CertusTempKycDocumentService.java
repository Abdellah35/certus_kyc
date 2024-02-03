package com.softedge.solution.service;

import com.softedge.solution.contractmodels.*;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertusTempKycDocumentService {


    List<KycProcessDocumentDetailsCM> requestUserTempKycDocument(HttpServletRequest request, ClientRequestDocumentsCM clientRequestDocumentsCM) throws KycDocumentGenericModuleException;

    List<KycProcessDocumentDetailsCM> getAllTempDocumentsResquestedByUserId(HttpServletRequest request, Long userId) throws KycDocumentGenericModuleException, UserAccountModuleException;


}