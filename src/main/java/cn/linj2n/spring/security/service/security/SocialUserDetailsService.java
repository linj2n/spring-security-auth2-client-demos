package cn.linj2n.spring.security.service.security;

import cn.linj2n.spring.security.config.support.SocialUserPrincipal;
import cn.linj2n.spring.security.domain.SocialUser;
import cn.linj2n.spring.security.service.SocialUserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SocialUserDetailsService extends CustomUserInfoTokenService {

    private static final String DELIMITER = "_";

    @Autowired
    private SocialUserService userService;

    public SocialUserDetailsService(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        if (map.containsKey("error")) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("userinfo returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        }
        SocialUser socialUserPrincipal = (SocialUser)getPrincipal(map);

        SocialUser socialUser =
                userService.findByLoginIdAndSourceType(socialUserPrincipal.getLogin(), socialUserPrincipal.getUserType())
                        .map(user -> {
                            user.setLogin(constructSocialUserName(socialUserPrincipal));
                            user.setUrl(socialUserPrincipal.getUrl());
                            user.setName(socialUserPrincipal.getName());
                            user.setEmail(socialUserPrincipal.getEmail());
                            user.setAvatarUrl(socialUserPrincipal.getAvatarUrl());
                            return userService.save(user);
                        })
                        .orElse(userService.createNewSocialUser(socialUserPrincipal));

        OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
                null, null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                socialUser, "N/A", socialUser.getAuthorities());
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }

    private String constructSocialUserName(SocialUser socialUser) {
        return socialUser.getLogin() + DELIMITER + socialUser.getUserType().name();
    }
}
