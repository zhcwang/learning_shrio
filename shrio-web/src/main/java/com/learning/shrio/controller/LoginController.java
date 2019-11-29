package com.learning.shrio.controller;

import com.learning.shrio.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String login(User user){
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPwd());
            token.setRememberMe(user.isRememberMe());
            subject.login(token);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if (subject.hasRole("admin")){
            return "有admin权限";
        } else {
            return "无admin权限";
        }
    }

    @RequiresRoles("admin")
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public String role(){
       return "test success";
    }

    /**
     * 使用自定义的Filter
     * @return
     */
    @RequestMapping(value = "/rolesor", method = RequestMethod.GET)
    public String rolesor(){
       return "test success";
    }
}
