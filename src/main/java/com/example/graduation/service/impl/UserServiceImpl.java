package com.example.graduation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.graduation.entity.User;
import com.example.graduation.mapper.UserMapper;
import com.example.graduation.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sr
 * @since 2019-01-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public boolean login(User user) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("id", user.getId());
        ew.eq("password", user.getPassword());
        return selectObj(ew) != null;
    }

    @Override
    public User getUser(String id) {
        return selectById(id);
    }
}
