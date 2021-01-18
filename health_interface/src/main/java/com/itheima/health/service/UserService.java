package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/13
 */
public interface UserService {
    /**
     * 通过用户名查询用户角色权限
     * @param username
     * @return
     */
    User findByUsername(String username);
}
