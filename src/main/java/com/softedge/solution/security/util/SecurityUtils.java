
package com.softedge.solution.security.util;


import com.softedge.solution.security.commons.JwtAuthenticationConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SecurityUtils {

    private  final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);


    @Autowired
    JwtAuthenticationConfig config;

    private SecurityUtils() {
    }

    public  String getUsernameFromToken(HttpServletRequest request) {
        String authorization = request.getHeader("authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            logger.info("JWT token {} ", token);
            Claims claims = Jwts.parser()
                    .setSigningKey(config.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            logger.info("Username {} ", username);
            return username;
        }
        else{
           return null;
        }
    }


}


/*

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    */
/**
 * Check if a user is authenticated.
 *
 * @return true if the user is authenticated, false otherwise
 *//*

    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(AuthoritiesConstants.USER)) {
                    return false;
                }
            }
        }
        return true;
    }

    */
/**
 * If the current user has a specific authority (security role).
 *
 * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
 *
 * @param authority the authority to check
 * @return true if the current user has the authority, false otherwise
 *//*

    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
            }
        }
        return false;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }
}
*/

