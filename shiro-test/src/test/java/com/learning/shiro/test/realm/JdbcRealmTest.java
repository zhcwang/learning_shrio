package com.learning.shiro.test.realm;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import javax.sql.DataSource;

public class JdbcRealmTest {
    private static DruidDataSource datasource = new DruidDataSource();
    static {
        datasource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=true");
        datasource.setUsername("root");
        datasource.setPassword("root");
    }
    public void testJdbcRealmCheck(){
        JdbcRealm realm = new JdbcRealm();
        realm.setDataSource(datasource);
        // 默认不开启查询permission表
        realm.setPermissionsLookupEnabled(true);
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
