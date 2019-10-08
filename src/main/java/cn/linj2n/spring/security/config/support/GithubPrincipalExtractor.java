package cn.linj2n.spring.security.config.support;

import cn.linj2n.spring.security.support.LoginType;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class GithubPrincipalExtractor implements PrincipalExtractor {

    private static final String GITHUB_UID_KEY = "id";

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        return new SocialUserPrincipal(
                map.get(GITHUB_UID_KEY).toString(), LoginType.GITHUB_USER);
    }
}