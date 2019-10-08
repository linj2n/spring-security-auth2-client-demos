package cn.linj2n.spring.security.service;

import cn.linj2n.spring.security.domain.SocialUser;
import cn.linj2n.spring.security.support.UserSourceType;

import java.util.Optional;

public class SocialUserServiceImpl implements SocialUserService {

    @Override
    public SocialUser createNewSocialUser(SocialUser newSocialUser) {
        return null;
    }

    @Override
    public Optional<SocialUser> findByLoginIdAndSourceType(String loginId, UserSourceType userSourceType) {
        return Optional.empty();
    }

    @Override
    public SocialUser save(SocialUser user) {
        return null;
    }
}
