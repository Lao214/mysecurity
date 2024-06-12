package com.example.mysecurity.aspect;/*
 *@title RefreshTokenAspect
 *@description
 *@author echoes
 *@version 1.0
 *@create 2024/6/12 15:53
 */

import com.example.mysecurity.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Aspect
@Component
public class RefreshTokenAspect {
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";


    @Autowired
    private StringRedisTemplate redisTemplate;

    //切所有返回值类型为ResponseResult的控制器方法
    @Around("execution(com.dmdd.userservice.util.ResponseResult com.dmdd.userservice.controller.*Controller.*(..))")
    public Object addToken(ProceedingJoinPoint joinPoint) {
        log.info("当前执行的方法：" + joinPoint.getSignature().getName());
        try {
            //执行原有方法
            ResponseResult result = (ResponseResult) joinPoint.proceed();
//                    MyResultEntity responseEntity= (MyResultEntity) result;
            //从redis读取token
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            //从security读用户名
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //判断redis中是否有token 只有第一次过期后才会创建token到redis中 第一次过期前因为没有存入token到redis中会死循环读取不到
            if (redisTemplate.hasKey("access-token:" + username) && redisTemplate.hasKey("refresh-token:" + username)) {
                System.out.println("redis 中的权限数据是：*******************" + "\n" + ops.get("access-token:" + username));
                result.setAccessToken(ops.get("access-token:" + username));
                result.setRefreshToken(ops.get("refresh-token:" + username));
                log.info("添加token:" + result);
                return result;
            }
            //从请求头读取token
            else {
                RequestAttributes ra = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                HttpServletRequest request = sra.getRequest();
                String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
                String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
                //从请求体中获取token
                if (accessToken.isEmpty() && refreshToken.isEmpty()) {
                    accessToken = request.getParameter(ACCESS_TOKEN_HEADER);
                    refreshToken = request.getParameter(REFRESH_TOKEN_HEADER);
                }
                result.setAccessToken(accessToken);
                result.setRefreshToken(refreshToken);
                return result;
            }
        } catch (Throwable throwable) {
            log.error("出现异常{}", throwable);
        }

        return null;
    }
}
