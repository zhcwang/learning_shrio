package com.learning.shrio.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * Session管理器
 */
public class CustomSessionManager extends DefaultWebSessionManager {
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest req = null;
        if (sessionKey instanceof WebSessionKey){
            req = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (req != null && sessionId != null){
            Session session = (Session) req.getAttribute(sessionId.toString());
            if (session != null) return session;
        }
        Session session = super.retrieveSession(sessionKey);
        if (req != null && sessionId != null){
            req.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
