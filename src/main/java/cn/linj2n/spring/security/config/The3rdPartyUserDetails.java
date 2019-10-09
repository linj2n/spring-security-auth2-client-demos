package cn.linj2n.spring.security.config;

import cn.linj2n.spring.security.support.UserSourceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
public class The3rdPartyUserDetails {
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
    private String url;
    private UserSourceType from;
    private Set<GrantedAuthority> authorities;
}
