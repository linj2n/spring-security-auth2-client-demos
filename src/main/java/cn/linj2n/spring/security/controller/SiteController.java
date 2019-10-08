package cn.linj2n.spring.security.controller;

import cn.linj2n.spring.security.domain.SocialUser;
import cn.linj2n.spring.security.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
public class SiteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping(value = "/user/me")
    public String userInfo(ModelMap map) {
        SecurityUtil.getSocialUseInfo().map(user -> {
            map.put("user", user);
            return user;
        });
        return "index";
    }

    @RequestMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }
}
