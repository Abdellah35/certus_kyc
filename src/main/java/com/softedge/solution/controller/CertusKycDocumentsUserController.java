package com.softedge.solution.controller;

import com.mongodb.DBObject;
import com.softedge.solution.contractmodels.KycProcessDocumentDetailsCM;
import com.softedge.solution.contractmodels.RequestedCompaniesCM;
import com.softedge.solution.contractmodels.UserSubmittedKycDocumentsCM;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import com.softedge.solution.service.CertusKycUserDocumentService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/kyc/user/documents")
public class CertusKycDocumentsUserController extends BaseController{

    @Autowired
    private CertusKycUserDocumentService certusKycUserDocumentService;

    @PostMapping("/attachment-image")
    public ResponseEntity<?> uploadAttachmentImage(@RequestParam("image") MultipartFile file){

        return certusKycUserDocumentService.uploadAttachmentImage(file);
    }

    @PostMapping("/save-document")
    public DBObject saveUserDocument(@RequestBody Document document, HttpServletRequest request){
        if(document.getInteger("docId")!=null) {
            return certusKycUserDocumentService.addUserDocument(request, document);
        }else{
            throw new KycDocumentGenericModuleException(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT);
        }
    }

    @PutMapping("/save-document")
    public DBObject updateUserDocument(@RequestBody Document document,  HttpServletRequest request){
        if(document.getInteger("docId")!=null) {
        return certusKycUserDocumentService.updateUserDocument(request, document);
        }else{
            throw new KycDocumentGenericModuleException(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT);
        }
    }

    @GetMapping("/get-document/docId/{docId}/user-id/{user-id}")
    public DBObject getUserDocument(@PathVariable Integer docId, @PathVariable("user-id") Long userId,  HttpServletRequest request){
        return certusKycUserDocumentService.getUserDocument(request, docId, userId);
    }

    @GetMapping("/get-document/docId/{docId}")
    public DBObject getUserDocument(@PathVariable Integer docId,  HttpServletRequest request){
        Long userId = null;
        return certusKycUserDocumentService.getUserDocument(request, docId, userId);
    }

    @DeleteMapping("/delete-document/docId/{docId}")
    public String deleteUserDocument(@PathVariable Integer docId,  HttpServletRequest request){
        Long userId = null;
        return certusKycUserDocumentService.deleteUserDocument(request, docId, userId);
    }

    @GetMapping("/get-user-documents")
    public ResponseEntity<?> getAllUserDocuments(HttpServletRequest request){
        return certusKycUserDocumentService.getAllDocumentUser(request);
    }


    @GetMapping("/company/{company-id}")
    public List<KycProcessDocumentDetailsCM> getAllRequestedDocumentsByCompany(HttpServletRequest request, @PathVariable("company-id")Long companyId){
        return certusKycUserDocumentService.getAllDocumentsResquestedByCompanyId(request, companyId);
    }

    @PostMapping("/submit-documents")
    public ResponseEntity<?> submitKycDocuments(HttpServletRequest request, @RequestBody UserSubmittedKycDocumentsCM userSubmittedKycDocumentsCM){
        return certusKycUserDocumentService.updateSubmittedDocumentsByUser(request, userSubmittedKycDocumentsCM);
    }

    @GetMapping("/companies")
    public List<RequestedCompaniesCM> getResquestedCompanies(HttpServletRequest request){
        return certusKycUserDocumentService.getRequestedCompanies(request);
    }

    @GetMapping(value = "/company/{company-id}/kyc-status")
    public ResponseEntity<?> getUserKycStatusSet(HttpServletRequest request, @PathVariable("company-id") Long companyId){
        return certusKycUserDocumentService.getUserKycStatusDetails(request,companyId);
    }

}
