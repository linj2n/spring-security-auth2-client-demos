package cn.linj2n.spring.security.domain;

import cn.linj2n.spring.security.support.UserSourceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {

    private String id;
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
    private String url;
    private UserSourceType userType;
    private Set<GrantedAuthority> authorities = new HashSet<>();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SocialUser{");
        sb.append("id='").append(id).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", userType=").append(userType);
        sb.append(", authorities=").append(authorities);
        sb.append('}');
        return sb.toString();
    }
}
