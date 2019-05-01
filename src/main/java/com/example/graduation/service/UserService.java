package com.example.graduation.service;

import com.example.graduation.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sr
 * @since 2019-01-18
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param user
     * @return
     */
    public boolean login(User user);

    /**
     * 获取用户信息判断是否为合法用户
     * @param id
     * @return
     */
    public User getUser(String id);
}
