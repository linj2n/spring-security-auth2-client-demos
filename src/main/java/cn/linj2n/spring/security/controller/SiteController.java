package cn.linj2n.spring.security.controller;

import cn.linj2n.spring.security.config.SecurityUtil;
import cn.linj2n.spring.security.config.The3rdPartyUserDetails;
import cn.linj2n.spring.security.domain.User;
import cn.linj2n.spring.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class SiteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/me")
    public String userInfo(ModelMap map) {
        if (SecurityUtil.isThe3rdPartyUser()) {
          The3rdPartyUserDetails userDetails = (The3rdPartyUserDetails) SecurityUtil.getCurrentPrincipal();
          userService.updateUserProfile(userDetails);
        }

        String login = SecurityUtil.getCurrentUserLogin();
        User user = userService.findByLogin(login);
        map.put("user", user);
        return "index";
    }

    @RequestMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }
}
