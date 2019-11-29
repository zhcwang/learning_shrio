package com.learning.shrio.dao;

import com.learning.shrio.pojo.User;

import java.util.List;

public interface UserDao {
    User getUserByAccount(String username);

    List<String> getRolesByAccount(String account);
}
