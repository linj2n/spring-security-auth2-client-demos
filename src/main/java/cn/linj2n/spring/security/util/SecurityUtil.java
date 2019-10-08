package cn.linj2n.spring.security.util;

import cn.linj2n.spring.security.config.support.AuthoritiesConstants;
import cn.linj2n.spring.security.config.support.SocialUserPrincipal;
import cn.linj2n.spring.security.domain.SocialUser;
import cn.linj2n.spring.security.support.UserSourceType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {

    private SecurityUtil () {
    }

    private static OAuth2Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2Authentication) {
            return (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        }
        return null;
    }

    public static Optional<SocialUser> getSocialUseInfo() {
       OAuth2Authentication oauth2Authentication = getAuthentication();
       if (oauth2Authentication == null) {
           return Optional.empty();
       }
       SocialUserPrincipal principal = (SocialUserPrincipal) oauth2Authentication.getPrincipal();
       Map userInfo = (Map)oauth2Authentication.getUserAuthentication().getDetails();
       return Optional.ofNullable(mapToSocialUser(userInfo, principal));
    }

}
