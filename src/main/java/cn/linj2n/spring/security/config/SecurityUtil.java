package cn.linj2n.spring.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {

    private SecurityUtil () {
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof The3rdPartyUserDetails){
                The3rdPartyUserDetails the3rdPartyUserDetails = (The3rdPartyUserDetails) authentication.getPrincipal();
                userName = the3rdPartyUserDetails.getLogin();
            } else if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            }
        }
        return userName;
    }

    public static boolean isThe3rdPartyUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication != null
                && authentication.getPrincipal() instanceof The3rdPartyUserDetails;
    }

    public static Object getCurrentPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = null;
        if (authentication != null) {
            principal = authentication.getPrincipal();
        }
        return principal;
    }


}
