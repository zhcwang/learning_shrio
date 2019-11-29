package com.learning.shrio.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 自定义权限过滤，可以配置灵活的权限策略
 */
public class RolesOrFilter extends AuthorizationFilter {
    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[])o;
        if (roles == null || roles.length == 0) return true;
        for (String role : roles) {
            if (subject.hasRole(role)) return true;
        }
        return false;
    }
}
