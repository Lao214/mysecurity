package com.example.mysecurity.utils;

public interface ResultCode {

    public  static  Integer SUCCESS = 200; //成功码

    public  static  Integer ERROR = 201;  //失败码

    Integer LOGIN_EXPIRED = 301;  //失败码
    Integer DENYACCESS = 300;
}
