package com.example.mysecurity.controller;


import com.example.mysecurity.entity.BUser;
import com.example.mysecurity.service.BUserService;
import com.example.mysecurity.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2023-02-18
 */

@RestController
@RequestMapping("/user")
public class BUserController {

    @Autowired
    private BUserService bUserService;



    @PostMapping("/addUser")
    private Result addUser(@RequestBody BUser bUser) {
        String gensalt = BCrypt.gensalt();
        String hashpw = BCrypt.hashpw(bUser.getPassword(), gensalt);
        bUser.setPassword(hashpw);
        bUser.setCreateTime(new Date());
        bUserService.save(bUser);
        return Result.success();
    }


}

