package com.softedge.solution.service;

import com.softedge.solution.contractmodels.UserCM;
import com.softedge.solution.exceptionhandlers.custom.bean.CountryException;
import com.softedge.solution.exceptionhandlers.custom.bean.StateException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.repomodels.UserRegistration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface CertusUserDetailsService {

    UserRegistration getUserByUsername(String username) throws UserAccountModuleException;


}
