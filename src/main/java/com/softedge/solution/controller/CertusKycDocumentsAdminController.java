package com.softedge.solution.controller;

import com.softedge.solution.repository.impl.MetadataDocumentDetailsRepositoryImpl;
import com.softedge.solution.service.CertusKycUserDocumentService;
import com.softedge.solution.service.impl.CertusKycDocumentServiceImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/kyc/documents/admin")
public class CertusKycDocumentsAdminController extends  BaseController{

    @Autowired
    MetadataDocumentDetailsRepositoryImpl metadataDocumentDetailsRepository;

    @Autowired
    private CertusKycDocumentServiceImpl certusKycDocumentService;

    @PostMapping(value = "/meta-load", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String documents(@RequestBody Document document) {
        boolean kycDocInsertion = metadataDocumentDetailsRepository.loadMetadataDocument(document);
        if(kycDocInsertion){
            return "doc added";
        }
        else{
            return "doc failed";
        }
    }
    @GetMapping(value = "/dashboard/kyc-summary")
    public ResponseEntity<?> getUserKycStatusSet(HttpServletRequest request, @RequestParam(value = "company-id", required = false) Long companyId){
        return certusKycDocumentService.getUserKycStatusSummary(request, companyId);
    }
}
