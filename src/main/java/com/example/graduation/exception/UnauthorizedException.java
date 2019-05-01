package com.example.graduation.exception;


import com.example.graduation.response.Response;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnauthorizedException extends RuntimeException {

        @ExceptionHandler(UnauthorizedException.class)
        public Response shiroException(){
            return Response.fail("你没有权限访问！请尝试重新登录 ");
        }

        @ExceptionHandler(AuthorizationException.class)
        public Response ShowException(){
            return Response.fail("你没有权限访问！请尝试重新登录 ");
        }

        @ExceptionHandler(IllegalStateException.class)
        public Response illegalException(){return Response.fail("你没有权限访问！请尝试重新登录");}



}
