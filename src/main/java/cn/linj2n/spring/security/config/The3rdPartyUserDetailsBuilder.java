package cn.linj2n.spring.security.config;

import cn.linj2n.spring.security.domain.User;
import cn.linj2n.spring.security.service.UserService;
import cn.linj2n.spring.security.support.UserSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier(value = "Github")
    private ClientResources github;

    private static final String SEPARATOR = "_";

    private static final String DEFAULT_NONE_VALUE = "";

    public The3rdPartyUserDetails build(Map<String, Object> map, UserSourceType userSourceType) {
        return build(map, getResource(userSourceType));
    }

    private The3rdPartyUserDetails build(Map<String, Object> map, ClientResources resources) {
        if (resources == null || map == null) {
            return null;
        }
        The3rdPartyUserDetails details = new The3rdPartyUserDetails();

        String login = map.getOrDefault(resources.getLoginKey(), DEFAULT_NONE_VALUE).toString();
        details.setLogin(reformatLogin(login, resources.getUserType()));

        details.setName(map.getOrDefault(resources.getUsernameKey(), DEFAULT_NONE_VALUE).toString());
        details.setEmail(map.getOrDefault(resources.getEmailKey(), DEFAULT_NONE_VALUE).toString());
        details.setAvatarUrl(map.getOrDefault(resources.getAvatarUrlKey(), DEFAULT_NONE_VALUE).toString());
        details.setUrl(map.getOrDefault(resources.getUrlKey(), DEFAULT_NONE_VALUE).toString());
        details.setFrom(resources.getUserType());

        details.setAuthorities(getDefaultAuthorities(resources.getUserType()));
        updateAuthorities(details);

        return details;
    }

    private void updateAuthorities(The3rdPartyUserDetails userDetails) {
        User userFromDB = userService.findByLoginAndUserSourceTypeName(userDetails.getLogin(), userDetails.getFrom().name());
        if (userFromDB == null) {
            userFromDB = userService.createNewUser(userDetails);
        }
        userDetails.getAuthorities().addAll(userFromDB.getAuthorities());
    }

    private String reformatLogin(String login, UserSourceType userSourceType) {
        return login + SEPARATOR + userSourceType.name();
    }

    private Set<GrantedAuthority> getDefaultAuthorities(UserSourceType userSourceType) {
        return new HashSet<>(AuthorityUtils.createAuthorityList(AuthoritiesConstants.The_3RD_PARTY_USER));
    }

    private ClientResources getResource(UserSourceType type) {
        if (type.equals(UserSourceType.GITHUB_USER)) {
            return github;
        }
        return null;
    }
}
