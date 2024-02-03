package com.softedge.solution.service.certusabstractservice;

import com.softedge.solution.repository.UserRepository;
import com.softedge.solution.security.util.SecurityUtils;
import com.softedge.solution.service.CertusKycDocumentService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CertusAbstractKycDocumentService extends CerterGenericErrorHandlingService implements CertusKycDocumentService {

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected UserRepository userRepository;

}
