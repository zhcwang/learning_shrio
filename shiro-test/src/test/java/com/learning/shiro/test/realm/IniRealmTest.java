package com.learning.shiro.test.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

public class IniRealmTest {
    public void testIniRealmCheck(){
        IniRealm realm = new IniRealm("classpath:user.ini");
        // 1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        securityManager.setRealm(realm);

        // 2、主体提交认证请求
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang.zhc", "test");
        subject.login(token);
        System.out.println("subject is authentication:" + subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkPermission("user:delete");
    }
}
