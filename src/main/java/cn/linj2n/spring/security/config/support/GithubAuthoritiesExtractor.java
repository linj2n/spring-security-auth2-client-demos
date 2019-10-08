package cn.linj2n.spring.security.config.support;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

public class GithubAuthoritiesExtractor
        implements AuthoritiesExtractor {

    @Override
    public List<GrantedAuthority> extractAuthorities
            (Map<String, Object> map) {
        return AuthorityUtils.createAuthorityList(
                AuthoritiesConstants.GITHUB_USER, AuthoritiesConstants.USER);
    }
}
