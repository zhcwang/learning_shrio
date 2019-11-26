package com.learning.shiro.test.realm;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * shiro jdbc认证通过三个维度auth、role、permission来做认证，使用三个标准表
 * 如果实际项目和默认表不一致，则可以通过自定义sql套用模板
 */
public class UserDefineJdbcRealmTest {
    private static DruidDataSource datasource = new DruidDataSource();
    static {
        datasource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=true");
        datasource.setUsername("root");
        datasource.setPassword("root");
    }

    @Test
    public void testUserDefineJdbcRealmCheck(){
        JdbcRealm realm = new JdbcRealm();
        realm.setDataSource(datasource);
        // 默认不开启查询permission表
        realm.setPermissionsLookupEnabled(true);
        String authSql = "select pwd from test_user where account = ?";
        realm.setAuthenticationQuery(authSql);
        // realm.setUserRolesQuery();
        // realm.setPermissionsQuery();
        // 1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        securityManager.setRealm(realm);
        // 2、主体提交认证请求
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang.zhc", "test");
        subject.login(token);
        System.out.println("subject is authentication:" + subject.isAuthenticated());
    }
}
