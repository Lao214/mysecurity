package com.example.mysecurity.filter;/*
 *@title RequestAuthenticationFilter
 *@description
 *@author echoes
 *@version 1.0
 *@create 2024/6/12 15:51
 */

import com.example.mysecurity.service.BUserService;
import com.example.mysecurity.utils.ApplicationContextUtils;
import com.example.mysecurity.utils.JwtUtils;
import com.example.mysecurity.utils.RsaUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 请求验证过滤器
 */
@Slf4j
public class RequestAuthenticationFilter extends BasicAuthenticationFilter {

    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    //通过工具类获得service对象
    private BUserService userService = ApplicationContextUtils.getBean(BUserService.class);

    //使用redis
    private StringRedisTemplate redisTemplate = ApplicationContextUtils.getBean(StringRedisTemplate.class);

    public RequestAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    //请求的过滤
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //从请求头获得token
        System.out.println("发送回来的请求是" + request + "\n" + "**************************************************");
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        System.out.println("accessToken是" + accessToken);
        if (StringUtils.isEmpty(accessToken)) {
            //从请求参数获得token
            accessToken = request.getParameter(accessToken);
        }
        //如果读取不到，就拦截
        if (StringUtils.isEmpty(accessToken)) {
            log.info("读取不到token，请求{}被拦截", request.getRequestURL());
            chain.doFilter(request, response);
            return;
        }
        try {
            parseToken(accessToken);
//            throw new ExpiredJwtException(new DefaultJwsHeader(new HashMap<>()), Jwts.claims().setSubject(""),"error");
        }
        //token过期
        catch (ExpiredJwtException ejex) {
            //如果access-token过时，则解析refresh-token
            String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
            if (StringUtils.isEmpty(refreshToken)) {
                log.info("读取不到refreshToken，请求{}被拦截", request.getRequestURL());
                chain.doFilter(request, response);
                return;
            }
            try {
                String username = parseToken(refreshToken);
                //生成新access-token，refresh-token
                accessToken = JwtUtils.generateToken(username, RsaUtils.privateKey, JwtUtils.EXPIRE_MINUTES);
                refreshToken = JwtUtils.generateToken(username, RsaUtils.privateKey, JwtUtils.EXPIRE_MINUTES * 5);
                log.info("重新生成access token：{}", accessToken);
                log.info("重新生成refresh token：{}", refreshToken);
                //将token保存到redis中
                redisTemplate.opsForValue().set("access-token:" + username, accessToken);
                redisTemplate.opsForValue().set("refresh-token:" + username, refreshToken);
            } catch (Exception ex) {
                log.error("解析token失败", ex);
            }
        } catch (Exception ex) {
            log.error("解析token失败", ex);
        }
        chain.doFilter(request, response);
    }

    /**
     * 对token进行解析然后通行
     */
    private String parseToken(String token) {
        //对token进行解析
        String username = JwtUtils.getUsernameFromToken(token, RsaUtils.publicKey);
        //将用户的权限查询出来
//            List<String> authList = userService.getAuthoritiesByUsername(username);
        List<String> authList = new ArrayList<String>();
        authList.add("dmdd");
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authList));
        //创建通行证
        UsernamePasswordAuthenticationToken authToken = new
                UsernamePasswordAuthenticationToken(username, "", authorities);
        //把通行证交给Security
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return username;
    }


}
