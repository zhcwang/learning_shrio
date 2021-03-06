package com.learning.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {
    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("wang.zhc", "test");
    }
    @Test
    public void testAuthentication(){
        // 1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        securityManager.setRealm(simpleAccountRealm);
        // 2、主体提交认证请求
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang.zhc", "test");
        subject.login(token);
        //subject.logout();
        System.out.println("subject is authentication:" + subject.isAuthenticated());
    }
}
