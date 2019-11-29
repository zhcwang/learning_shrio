package com.learning.shrio.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Session存储的具体实现类
 */
@Component
public class RedisSessionDao extends AbstractSessionDAO {
    @Autowired
    JedisUtils jedisUtils;

    private static final String SESSION_PREFIX = "shrio-session:";

    private byte[] getKey(String key){
        return (SESSION_PREFIX + key).getBytes();
    }

    /**
     * 访问首页时，如果没有session则优先创建session
     * 此时session只有基本请求{@link SavedRequest}，没什么有用的属性，所以setAttr方法并不适用
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        System.out.println("create new session:" + sessionId);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    /**
     * 登录时查询session,然后不管成功失败立即更新一次session
     * 登录后如果要获取role信息，则继续调用读取session的方法，之后运行更新
     * 此时session中就会存放{@link org.apache.shiro.subject.support.DefaultSubjectContext#AUTHENTICATED_SESSION_KEY} true,表示登录成功了
     * 还有账号信息，还要基本请求{@link SavedRequest}
     * 在此步骤可以根据AUTHENTICATED_SESSION_KEY 进一步获取权限等信息放入attr中
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("get session :" + sessionId);
        if (sessionId == null) return null;
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtils.getValue(key);
        Session session = (Session)SerializationUtils.deserialize(value);
        Collection<Object> attributeKeys = session.getAttributeKeys();
        attributeKeys.forEach((object) -> {
            System.out.println(object); // shiroSavedRequest
            System.out.println(session.getAttribute(object)); //SavedRequest
        });
        return session;
    }

    private void saveSession(Session session){
        if (session != null && session.getId() != null){
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtils.set(key, value);
            jedisUtils.expire(key, 600);
        }
    }

    /**
     * 如果没登录，则session就记录了{@link SavedRequest},
     * 如果登录验证通过了，则会记录{@link org.apache.shiro.subject.support.DefaultSubjectContext#PRINCIPALS_SESSION_KEY} 账号 wang.zhc
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        System.out.println("update session：" + session.getId());
        Collection<Object> attributeKeys = session.getAttributeKeys();
        attributeKeys.forEach((object) -> {
            System.out.println(object); // shiroSavedRequest
            System.out.println(session.getAttribute(object)); // SavedRequest
        });
        saveSession(session);
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null){
            byte[] key = getKey(session.getId().toString());
            jedisUtils.delete(key);
        }
    }

    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtils.keys(SESSION_PREFIX);
        Set<Session> sessions = new HashSet<Session>();
        if (keys != null) {
            for (byte[] key : keys) {
                byte[] value = jedisUtils.getValue(key);
                Session session  = (Session) SerializationUtils.deserialize(value);
                sessions.add(session);
            }
        }
        return sessions;
    }
}
