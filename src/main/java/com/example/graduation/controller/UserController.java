package com.example.graduation.controller;


import com.example.graduation.entity.User;
import com.example.graduation.jwt.JWTUtil;
import com.example.graduation.response.Response;
import com.example.graduation.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-01-18
 */
@Api(tags = "系统登录Api")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public Response login(@RequestBody User user) {
        if (userService.login(user)) {
            return Response.success(JWTUtil.sign(user.getId(), user.getPassword()));
        }
        return Response.fail("用户名或密码错误");
    }


}

