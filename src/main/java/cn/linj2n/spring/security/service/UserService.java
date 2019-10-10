package cn.linj2n.spring.security.service;

import cn.linj2n.spring.security.config.The3rdPartyUserDetails;
import cn.linj2n.spring.security.domain.User;

public interface UserService {

    User createNewUser(The3rdPartyUserDetails the3rdPartyUserDetails);

    User getCurrentLoginUserInfo();

    User findByLoginAndUserSourceTypeName(String login, String sourceTypeName);

    void updateUserProfile(The3rdPartyUserDetails userDetails);

    User findByLogin(String login);
}
