package com.softedge.solution.security.util;

import com.softedge.solution.contractmodels.AuthenticateUserCM;
import com.softedge.solution.repomodels.UserRegistration;
import com.softedge.solution.security.commons.JwtAuthenticationConfig;
import com.softedge.solution.security.models.AuthenticationResponse;
import com.softedge.solution.security.models.Authorities;
import com.softedge.solution.security.models.PasswordResetResponse;
import com.softedge.solution.service.CertusUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class RegistrationUtility {




    @Autowired
    JwtAuthenticationConfig config;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    private CertusUserDetailsService userDetailsService;


    public ResponseEntity<?> authenticateUser(AuthenticateUserCM authenticateUserCM) {
        UserRegistration registeredUser = userDetailsService.getUserByUsername(authenticateUserCM.getUsername());
        if (registeredUser != null) {
            if (passwordEncoder.matches(authenticateUserCM.getPassword(), registeredUser.getPassword())) {
                return this.generateToken(registeredUser);
            } else {
                return new ResponseEntity("Authentication failure", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.ok(new PasswordResetResponse("Password Reset failure"));
        }
    }

    private ResponseEntity<?> generateToken(UserRegistration userRegistration) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .setSubject(userRegistration.getUsername())
                .claim("authorities", userRegistration.getAuthorities().stream()
                        .map(Authorities::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(config.getExpiration())))
                .signWith(SignatureAlgorithm
                        .HS256, config.getSecret().getBytes())
                .compact();

        return ResponseEntity.ok(new AuthenticationResponse(token));

    }
}
