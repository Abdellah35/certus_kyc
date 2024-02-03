package com.softedge.solution.security.commons;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Config JWT.
 * Only one property 'shuaicj.security.jwt.secret' is mandatory.
 *
 * @author shuaicj 2017/10/18
 */
@Getter
@ToString
@Component
public class JwtAuthenticationConfig {


    @Value("${shuaicj.security.jwt.expiration:#{1440 * 60 * 60 * 60 * 1000}}")
    private int expiration; // default 24 hrs

    @Value("${shuaicj.security.jwt.secret}")
    private String secret;
}
