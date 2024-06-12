package com.example.mysecurity;

import com.example.mysecurity.entity.BUser;
import com.example.mysecurity.service.BUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;


@SpringBootTest
class MySecurityApplicationTests {

	@Autowired
	private BUserService userService;


 	@Test
	void lambdaFor() {

	}

	@Test
	void contextLoads() {

	}



}
