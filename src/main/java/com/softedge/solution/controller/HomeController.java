package com.softedge.solution.controller;

import com.softedge.solution.repomodels.UserRegistration;
import com.softedge.solution.repository.UserRepository;
import com.softedge.solution.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/kyc/documents")
public class HomeController {

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public String hello(HttpServletRequest request){

        String username = this.securityUtils.getUsernameFromToken(request);
        UserRegistration userRegistration =  userRepository.findByUsername(username);
        return userRegistration.getName()+" "+userRegistration.getGender();
    }
}
