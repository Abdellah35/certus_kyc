package com.softedge.solution.controller;

import com.softedge.solution.contractmodels.KYCStatusCM;
import com.softedge.solution.contractmodels.KycProcessDocumentDetailsCM;
import com.softedge.solution.contractmodels.ClientRequestDocumentsCM;
import com.softedge.solution.contractmodels.ProcessRequestCM;
import com.softedge.solution.service.impl.CertusKycDocumentServiceImpl;
import com.softedge.solution.service.impl.CertusTempKycDocumentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/kyc/company/documents")
public class CertusKycDocumentCompanyController extends BaseController{

    private final Logger log = LoggerFactory.getLogger(CertusKycDocumentCompanyController.class);

    @Autowired
    private CertusKycDocumentServiceImpl certusKycDocumentService;


    @Autowired
    private CertusTempKycDocumentServiceImpl certusTempKycDocumentService;

    @PostMapping(value = "/request-document")
    public List<KycProcessDocumentDetailsCM> requestKycdocument(HttpServletRequest request, @RequestBody ClientRequestDocumentsCM clientRequestDocumentsCM, @RequestParam(value="unregistered",required = false) Boolean unregistered){
        if(unregistered==null||unregistered==false){
            return certusKycDocumentService.requestUserKycDocument(request, clientRequestDocumentsCM);
        }
        else{
            return certusTempKycDocumentService.requestUserTempKycDocument(request, clientRequestDocumentsCM);
        }

    }

    @GetMapping(value = "/user/{user-id}")
    public List<KycProcessDocumentDetailsCM> getKycdocumentOfUser(HttpServletRequest request, @PathVariable("user-id")Long userId, @RequestParam(value = "unregistered",required = false) Boolean unregistered){
        if(unregistered==null||unregistered==false) {
            return certusKycDocumentService.getAllDocumentsResquestedByUserId(request, userId);
        }else{
            return certusTempKycDocumentService.getAllTempDocumentsResquestedByUserId(request, userId);
        }
    }

    @PostMapping(value = "/process-request")
    public ResponseEntity<?> documentProcessRequest(HttpServletRequest request, @RequestBody ProcessRequestCM processRequestCM,  @RequestParam(value="unregistered",required = false) Boolean unregistered){
        return certusKycDocumentService.processStatusUpdate(request,processRequestCM);
    }

    @DeleteMapping(value = "/kyc/{kyc-id}/delete")
    public ResponseEntity<?> documentDelete(HttpServletRequest request, @PathVariable("kyc-id") Long kycId){
        return certusKycDocumentService.deleteKycDocumentByKycId(request,kycId);
    }

    @PostMapping(value = "/user/{user-id}/kyc-status")
    public ResponseEntity<?> userKycStatuSave(HttpServletRequest request, @PathVariable("user-id") Long userId,@RequestBody KYCStatusCM kycStatusCM, @RequestParam(value = "unregistered",required = false) Boolean unregistered){
        if(kycStatusCM.getStatus()==null){
            kycStatusCM.setStatus("");
        }
        if(unregistered==null||unregistered==false) {
            unregistered = false;
        }
        return certusKycDocumentService.upsertUserKYCStatusDetails(request,userId, kycStatusCM.getStatus(), unregistered);
    }

    @PutMapping(value = "/user/{user-id}/kyc-status")
    public ResponseEntity<?> userKycStatusUdpate(HttpServletRequest request,  @PathVariable("user-id") Long userId, @RequestBody KYCStatusCM kycStatusCM, @RequestParam(value = "unregistered",required = false) Boolean unregistered){
        if(kycStatusCM.getStatus()==null){
            kycStatusCM.setStatus("");
        }
        if(unregistered==null||unregistered==false) {
            unregistered = false;
        }
        return certusKycDocumentService.upsertUserKYCStatusDetails(request,userId, kycStatusCM.getStatus(), unregistered);
    }

    @GetMapping(value = "/user/{user-id}/kyc-status")
    public ResponseEntity<?> getUserKycStatusSet(HttpServletRequest request, @PathVariable("user-id") Long userId){
        return certusKycDocumentService.getUserKycStatusDetails(request,userId);
    }

    @GetMapping(value = "/dashboard/kyc-summary")
    public ResponseEntity<?> getUserKycStatusSummary(HttpServletRequest request){
        Long companyId = null;
        return certusKycDocumentService.getUserKycStatusSummary(request, companyId);
    }
}
