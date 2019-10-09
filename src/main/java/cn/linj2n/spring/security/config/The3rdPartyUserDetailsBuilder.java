package cn.linj2n.spring.security.config;

import cn.linj2n.spring.security.config.support.AuthoritiesConstants;
import cn.linj2n.spring.security.domain.User;
import cn.linj2n.spring.security.service.UserService;
import cn.linj2n.spring.security.support.UserSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class The3rdPartyUserDetailsBuilder {

    @Autowired
    private  UserService userService;

    private static final String SEPARATOR = "_";

    public The3rdPartyUserDetails build(Map<String, Object> map, UserSourceType userSourceType) {
        The3rdPartyUserDetails the3rdPartyUserDetails = null;
        if (userSourceType.equals(UserSourceType.GITHUB_USER)) {
            the3rdPartyUserDetails = buildGithubUserDetails(map);
            User userFromDB = userService.findByLoginAndUserSourceTypeName(the3rdPartyUserDetails.getLogin(), UserSourceType.GITHUB_USER.name());
            if (userFromDB == null) {
                userFromDB = userService.createNewUser(the3rdPartyUserDetails);
            }
            the3rdPartyUserDetails.getAuthorities().addAll(userFromDB.getAuthorities());
        }
        return the3rdPartyUserDetails;
    }

    private The3rdPartyUserDetails buildGithubUserDetails(Map<String, Object> map) {
        String login = map.get("login").toString()
                .concat(SEPARATOR).concat(UserSourceType.GITHUB_USER.name());
        String email = map.get("email").toString();
        Set<GrantedAuthority> authorities = new HashSet<>(AuthorityUtils.createAuthorityList(UserSourceType.GITHUB_USER.name(), AuthoritiesConstants.USER));
        The3rdPartyUserDetails details = new The3rdPartyUserDetails();
        details.setEmail(email);
        details.setLogin(login);
        details.setAuthorities(authorities);
        details.setFrom(UserSourceType.GITHUB_USER);
        return details;
    }
}
