package cn.linj2n.spring.security.service;

import cn.linj2n.spring.security.config.SecurityUtil;
import cn.linj2n.spring.security.config.The3rdPartyUserDetails;
import cn.linj2n.spring.security.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private Map<String, User> userRepository = new ConcurrentHashMap<>();

    @Override
    public User createNewUser(The3rdPartyUserDetails the3rdPartyUserDetails) {
        User user = mapToUser(the3rdPartyUserDetails);
        userRepository.put(user.getLogin(), user);
        return user;
    }

    @Override
    public User getCurrentLoginUserInfo() {
        if (SecurityUtil.isThe3rdPartyUser()) {
            The3rdPartyUserDetails userDetails = (The3rdPartyUserDetails) SecurityUtil.getCurrentPrincipal();
            updateUserProfile(userDetails);
        }
        String login = SecurityUtil.getCurrentUserLogin();
        return findByLogin(login);
    }

    @Override
    public User findByLoginAndUserSourceTypeName(String login, String sourceTypeName) {
        User user = userRepository.get(login);
        if (user == null || !user.getUserType().name().equals(sourceTypeName)) {
            return null;
        }
        return user;
    }

    @Override
    public void updateUserProfile(The3rdPartyUserDetails userDetails) {
        LOGGER.info("Updating user profile from user details. ");
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.get(login);
    }

    private User mapToUser(The3rdPartyUserDetails details) {
        User user = new User();
        user.setLogin(details.getLogin());
        user.setUserType(details.getFrom());
        user.setEmail(details.getEmail());
        user.setAvatarUrl(details.getAvatarUrl());
        user.setAuthorities(details.getAuthorities());
        user.setUrl(details.getUrl());
        return user;
    }
}
