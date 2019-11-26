package com.learning.shiro.test.realm.po;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import sun.security.provider.MD5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 如果生产中实际环境认证机制不局限与库，比如微服务认证中心，则可以使用自定义Real来完成网络认证
 */
public class UserDefineRealm extends AuthorizingRealm {


    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 1、 从主体中获取用户名
        String account = (String)principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByAccout(account);
        Set<String> permissions = getPermissionsByRoles(roles);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getRolesByAccout(String account) {
        HashSet<String> roles = new HashSet<String>();
        roles.add("admin");
        return roles;
    }

    private Set<String> getPermissionsByRoles(Set<String> roles) {
        HashSet<String> permissions = new HashSet<String>();
        permissions.add("user:delete");
        permissions.add("user:update");
        return permissions;
    }


    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1、 从主体中获取用户名
        String account = (String)authenticationToken.getPrincipal();
        // 2、 到认证中心认证
        String pwd = getPwdByAccount(account);
        if (pwd == null) return null;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, pwd, "xxx");
        info.setCredentialsSalt(ByteSource.Util.bytes("random-salt"));
        return info;
    }

    private String getPwdByAccount(String account) {
        System.out.println(account);
        Md5Hash md5Hash = new Md5Hash("test", "random-salt");
        return md5Hash.toString();
        //return "test";
    }
}
