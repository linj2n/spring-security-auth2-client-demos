package cn.linj2n.spring.security.service;

import cn.linj2n.spring.security.config.The3rdPartyUserDetails;
import cn.linj2n.spring.security.domain.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService{

    private Map<String, User> userMap = new ConcurrentHashMap<>();

    @Override
    public User createNewUser(The3rdPartyUserDetails the3rdPartyUserDetails) {
        User user = new User();
        user.setLogin(the3rdPartyUserDetails.getLogin());
        user.setEmail(the3rdPartyUserDetails.getEmail());
        user.setAuthorities(the3rdPartyUserDetails.getAuthorities());
        user.setUserType(the3rdPartyUserDetails.getFrom());
        userMap.put(user.getLogin(), user);
        return user;
    }

    @Override
    public User findByLoginAndUserSourceTypeName(String login, String sourceTypeName) {
        User user = userMap.get(login);
        if (user == null || !user.getUserType().name().equals(sourceTypeName)) {
            return null;
        }
        return user;
    }

    @Override
    public void updateUserProfile(The3rdPartyUserDetails userDetails) {
        System.out.println("update user profile from user details. ");
    }

    @Override
    public User findByLogin(String login) {
        return userMap.get(login);
    }
}
