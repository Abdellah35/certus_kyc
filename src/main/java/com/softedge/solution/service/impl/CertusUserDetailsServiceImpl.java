package com.softedge.solution.service.impl;

import com.softedge.solution.contractmodels.MapperObjectCM;
import com.softedge.solution.contractmodels.UserAddressCM;
import com.softedge.solution.contractmodels.UserCM;
import com.softedge.solution.contractmodels.UserSessionCM;
import com.softedge.solution.exceptionhandlers.custom.bean.CountryException;
import com.softedge.solution.exceptionhandlers.custom.bean.StateException;
import com.softedge.solution.exceptionhandlers.custom.user.UserAccountModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeUserEnum;
import com.softedge.solution.repomodels.Country;
import com.softedge.solution.repomodels.Location;
import com.softedge.solution.repomodels.State;
import com.softedge.solution.repomodels.UserRegistration;
import com.softedge.solution.service.certusabstractservice.CertusAbstractUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CertusUserDetailsServiceImpl extends CertusAbstractUserDetailsService {


    @Value("${user.image.dir}")
    private String userImageDir;

    @Value("${user.logodir}")
    private String userLogoDir;


    @Value("${base.url}")
    private String baseUrl;

    Path fileStorageLocation = Paths.get("/usr/share/nginx/html/certus-user-service/uploadlogo/").toAbsolutePath().normalize();

    @Override
    @Transactional(readOnly = true)
    public UserRegistration getUserByUsername(String username) throws UserAccountModuleException {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw this.errorService(ErrorCodeUserEnum.INTERNAL_SERVER_ERROR.getErrorCode(), e);
        }
    }



}
