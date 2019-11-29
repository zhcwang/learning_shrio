package com.learning.shrio.dao;

import com.learning.shrio.pojo.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao  {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUserByAccount(String username) {
        String sql = "select username,password from users where username = ?";
        List<User> users = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setName(resultSet.getString("username"));
                user.setPwd(resultSet.getString("password"));
                return user;
            }
        });
        if (CollectionUtils.isEmpty(users)){
            return null;
        }
        return users.get(0);
    }

    public List<String> getRolesByAccount(String account) {
        String sql = "select role_name from user_roles where username = ?";
        return jdbcTemplate.query(sql, new String[]{account}, new RowMapper<String>() {
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
    }
}
