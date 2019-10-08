package cn.linj2n.spring.security.config.support;

import cn.linj2n.spring.security.support.UserSourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialUserPrincipal {
    private String login;
    private UserSourceType userSourceType;

    public SocialUserPrincipal(String login, UserSourceType userSourceType) {
        this.login = login;
        this.userSourceType = userSourceType;
    }
}
