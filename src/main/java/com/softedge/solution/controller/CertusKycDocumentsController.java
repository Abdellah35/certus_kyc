package com.softedge.solution.controller;

import com.mongodb.DBObject;
import com.softedge.solution.contractmodels.KycDocumentMetaCM;
import com.softedge.solution.contractmodels.KycDocumentMetaOverviewCM;
import com.softedge.solution.feignbeans.UserIPVActivation;
import com.softedge.solution.proxy.CertusRedisCacheServiceProxy;
import com.softedge.solution.proxy.CertusUserServiceProxy;
import com.softedge.solution.service.impl.CertusKycDocumentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/kyc/documents")
public class CertusKycDocumentsController extends  BaseController{

    private final Logger log = LoggerFactory.getLogger(CertusKycDocumentsController.class);

    @Autowired
    private CertusUserServiceProxy proxy;
    @Autowired
    private CertusRedisCacheServiceProxy certusRedisCacheServiceProxy;

    @Autowired
    private CertusKycDocumentServiceImpl certusKycDocumentService;

    @GetMapping("/feign-client/greet")
    public String greetClient(){
        return proxy.greet();
    }

    @GetMapping("/feign-client/wish")
    public String wishClient(){
        return proxy.wish();
    }

    @GetMapping("/feign-client/hello")
    public String helloClient(){
        return certusRedisCacheServiceProxy.hello();
    }

    @PostMapping("/feign-client/redis/user-ipv")
    public UserIPVActivation add(@RequestBody UserIPVActivation userIPVActivation){
        return certusRedisCacheServiceProxy.add(userIPVActivation);
    }

    @GetMapping("/get-documents-meta")
    public List<KycDocumentMetaCM> getKycDocuments(HttpServletRequest request) {
        return certusKycDocumentService.getAllKycDocumentsMeta(request);
    }

    @GetMapping("/get-documents-meta/overview")
    public List<KycDocumentMetaOverviewCM> getKycDocumentsOverview(HttpServletRequest request) {
        return certusKycDocumentService.getAllKycDocumentsMetaClientOverview(request);
    }

    @GetMapping("/get-document-meta/{id}")
    public DBObject getKycDocumentMetaById(@PathVariable Long id) {
        return certusKycDocumentService.getKycMetaDocumentById(id);
    }


    @GetMapping("/feign-client/redis/user-ipv")
    public UserIPVActivation getIPVByEmailId(@RequestParam("email-id")String emailId){
        log.info("The emailid is :: {}", emailId);
        return certusRedisCacheServiceProxy.findById(emailId);
    }
}
