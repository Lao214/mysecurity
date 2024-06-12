package com.example.mysecurity.config;/*
 *@title SecurityConfig
 *@description
 *@author echoes
 *@version 1.0
 *@create 2024/6/12 15:49
 */


import com.example.mysecurity.filter.RequestAuthenticationFilter;
import com.example.mysecurity.handler.LoginSuccessHandler;
import com.example.mysecurity.utils.ResponseResult;
import com.example.mysecurity.utils.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SpringSecurity的核心配置
 */
//启动权限控制的注解
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
//启动Security的验证
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //提供密码编码器
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    //配置验证用户的账号和密码
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //数据库用户验证
        auth.userDetailsService(userDetailsService);
    }

    //配置访问控制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //给请求授权
        http.authorizeRequests()
                //给登录相关的请求放行
                .antMatchers("/login","/logout","/").permitAll()
                //访问控制
                //其余的都拦截
                .anyRequest().authenticated()
                .and()
                //配置自定义登录
                .formLogin()
                .successHandler(loginSuccessHandler)//成功处理器
                .failureHandler(((httpServletRequest, httpServletResponse, e) -> { //登录失败处理器
                    ResponseResult.write(httpServletResponse, ResponseResult.error(ResponseStatus.LOGIN_ERROR));
                }))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(((httpServletRequest, httpServletResponse, e) -> { //未验证处理
                    ResponseResult.write(httpServletResponse,ResponseResult.error(ResponseStatus.AUTH_ERROR));
                }))
                .and()
                .logout() //配置注销
                .logoutSuccessHandler(((httpServletRequest, httpServletResponse, authentication) -> { //注销成功
                    ResponseResult.write(httpServletResponse,ResponseResult.ok(ResponseStatus.OK));
                }))
                .clearAuthentication(true) //清除验证信息
                .and()
                .cors() //配置跨域
                .configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable() //停止csrf
                .sessionManagement() //session管理
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //无状态，不使用session
                .and()
                .addFilter(new RequestAuthenticationFilter(authenticationManager())) //添加自定义验证过滤器
        ;
    }

    /**
     * 跨域配置对象
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //配置允许访问的服务器域名
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}