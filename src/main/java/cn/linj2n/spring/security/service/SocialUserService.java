package cn.linj2n.spring.security.service;

import cn.linj2n.spring.security.domain.SocialUser;
import cn.linj2n.spring.security.support.UserSourceType;

import java.util.Optional;

public interface SocialUserService {

    SocialUser createNewSocialUser(SocialUser newSocialUser);

    Optional<SocialUser> findByLoginIdAndSourceType(String loginId, UserSourceType userSourceType );

    SocialUser save(SocialUser user);
}
