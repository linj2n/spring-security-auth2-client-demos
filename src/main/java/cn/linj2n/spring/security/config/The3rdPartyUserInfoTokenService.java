package cn.linj2n.spring.security.service.security;

import cn.linj2n.spring.security.config.The3rdPartyUserDetails;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Set;

public class The3rdPartyUserInfoTokenService implements ResourceServerTokenServices{

    private UserInfoTokenServices userInfoTokenServices;

    public The3rdPartyUserInfoTokenService(UserInfoTokenServices userInfoTokenServices) {
        super();
        this.userInfoTokenServices = userInfoTokenServices;
    }

    public The3rdPartyUserInfoTokenService(String userInfoEndpointUrl, String clientId) {
        this(new UserInfoTokenServices(userInfoEndpointUrl, clientId));
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication authentication = userInfoTokenServices.loadAuthentication(accessToken);
        The3rdPartyUserDetails principal = (The3rdPartyUserDetails) authentication.getPrincipal();

        Set<GrantedAuthority> authorities = principal.getAuthorities();
        authorities.addAll(authentication.getAuthorities());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        token.setDetails(authentication.getDetails());
        return new OAuth2Authentication(authentication.getOAuth2Request(), token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return userInfoTokenServices.readAccessToken(accessToken);
    }

    public void setTokenType(String tokenType) {
        userInfoTokenServices.setTokenType(tokenType);
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        userInfoTokenServices.setRestTemplate(restTemplate);
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        userInfoTokenServices.setAuthoritiesExtractor(authoritiesExtractor);
    }

    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        userInfoTokenServices.setPrincipalExtractor(principalExtractor);
    }

}
