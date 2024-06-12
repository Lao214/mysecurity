package com.example.mysecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mysecurity.entity.BUser;
import com.example.mysecurity.service.BUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 该模块 从数据库查询登陆的用户信息以及该用户所拥有的权限，然后交给security管理
 *
 * 进行一系列的安全验证。详细执行流程如下
 *
 * 1.用户输入用户名和密码点击登陆
 *
 * 2.该方法会拦截到请求查询到数据库的用户和该用户的权限字符串
 *
 * 3.交给security进行验证。
 *
 * 注：该方法调用了工具类的方法，记得导入工具类！
 */

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private BUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BUser user = userService.getOne(new QueryWrapper<BUser>().lambda().eq(BUser::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");

        }
        //设置权限字符串为null
        String auths = "echoes";
        return new User(user.getUsername(), user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(auths));
    }
}