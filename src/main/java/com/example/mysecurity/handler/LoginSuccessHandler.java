package com.example.mysecurity.handler;/*
 *@title LoginSuccessHandler
 *@description
 *@author echoes
 *@version 1.0
 *@create 2024/6/12 15:50
 */


import com.example.mysecurity.utils.JwtUtils;
import com.example.mysecurity.utils.ResponseResult;
import com.example.mysecurity.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理器11133
 */
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    //登录成功的回调
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //获得用户名
        User user = (User) authentication.getPrincipal();
        //生成token字符串
        String accessToken = JwtUtils.generateToken(user.getUsername(), RsaUtils.privateKey, JwtUtils.EXPIRE_MINUTES);
        //生成refresh token字符串
        String refreshToken = JwtUtils.generateToken(user.getUsername(), RsaUtils.privateKey, JwtUtils.EXPIRE_MINUTES * 50);
        log.info("生成refresh token：{}",refreshToken);
        log.info("生成accessToken：{}",accessToken);
        //发送token给前端11
        ResponseResult.write(httpServletResponse,ResponseResult.okTwo   (user.getUsername(),accessToken,refreshToken));
    }
}
