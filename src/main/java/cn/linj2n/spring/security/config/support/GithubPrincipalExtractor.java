package cn.linj2n.spring.security.config.support;

import cn.linj2n.spring.security.config.The3rdPartyUserDetailsBuilder;
import cn.linj2n.spring.security.support.UserSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class GithubPrincipalExtractor implements PrincipalExtractor {

    @Autowired
    private The3rdPartyUserDetailsBuilder the3rdPartyUserDetailsBuilder;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        return the3rdPartyUserDetailsBuilder.build(map, UserSourceType.GITHUB_USER);
    }
}