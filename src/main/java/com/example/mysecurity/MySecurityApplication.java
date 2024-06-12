package com.example.mysecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example")
@MapperScan("com.example.mysecurity.dao")
@SpringBootApplication
public class MySecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySecurityApplication.class, args);
	}

}


/**
 *
 * 客户端登陆成功后，发送的请求将会被该类进行过滤，读取请求中的token，判断token是否过期。讲解一下其中重要的操作
 *
 * 1.
 * public static final String ACCESS_TOKEN_HEADER = "Authorization";
 * String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
 * 从请求的请求头中获取"Authorization"保存的token
 *
 * 2.
 * accessToken = request.getParameter(ACCESS_TOKEN_HEADER);
 * 从请求体中读取token
 *
 *
 * 3.
 * throw new ExpiredJwtException(new DefaultJwsHeader(new HashMap<>()), Jwts.claims().setSubject(""),"error");
 * 手动抛出token过期异常，用于测试双token刷新机制。
 *
 *
 * 4.
 * //生成新access-token，refresh-token
 * accessToken = JwtUtils.generateToken(username, RsaUtils.privateKey, JwtUtils.EXPIRE_MINUTES);
 * refreshToken = JwtUtils.generateToken(username, RsaUtils.privateKey, JwtUtils.EXPIRE_MINUTES * 50);
 * 调用工具类生成新的token
 *
 * 5.
 *
 * //将token保存到redis中
 * redisTemplate.opsForValue().set("access-token:" + username, accessToken);
 * redisTemplate.opsForValue().set("refresh-token:" + username, refreshToken);
 * 将双token保存到redis中
 *
 * 6.
 * List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authList));
 *  将查询到的权限字符串集合转换成security能够接收的类型。
 *
 *
 * https://blog.csdn.net/qq_60614034/article/details/129781196
 */