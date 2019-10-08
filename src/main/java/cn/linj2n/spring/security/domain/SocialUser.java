package cn.linj2n.spring.security.domain;

import cn.linj2n.spring.security.support.UserSourceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SocialUser {
    private String id;
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
    private String url;
    private UserSourceType userType;
    private Set<GrantedAuthority> authorities = new HashSet<>();
}
