package com.learning.shrio.security.realm;

import com.learning.shrio.dao.UserDao;
import com.learning.shrio.pojo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Realm主要是用来做认证以及权限校验的
 * 如果生产中实际环境认证机制不局限与库，比如微服务认证中心，则可以使用自定义Realm来完成网络认证
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserDao userDao;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("从数据库中获取权限信息。");
        // 1、 从主体中获取用户名
        String account = (String)principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByAccount(account);
        Set<String> permissions = getPermissionsByRoles(roles);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getRolesByAccount(String account) {
        List<String> roles = userDao.getRolesByAccount(account);
        return new HashSet<String>(roles);
    }

    private Set<String> getPermissionsByRoles(Set<String> roles) {
        HashSet<String> permissions = new HashSet<String>();
        permissions.add("user:delete");
        permissions.add("user:update");
        return permissions;
    }


    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("从数据库中获取认证信息。");
        // 1、 从主体中获取用户名
        String account = (String)authenticationToken.getPrincipal();
        // 2、 到认证中心认证
        String pwd = getPwdByAccount(account);
        if (pwd == null) return null;
        Md5Hash md5Hash = new Md5Hash(pwd);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, md5Hash.toString(), "xxx");
        return info;
    }

    private String getPwdByAccount(String account) {
        User user = userDao.getUserByAccount(account);
        if(user != null){
            return user.getPwd();
        }
        return null;
    }
}
