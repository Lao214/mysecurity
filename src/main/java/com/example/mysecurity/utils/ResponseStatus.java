package com.example.mysecurity.utils;/*
 *@title ResponseStatus
 *@description
 *@author echoes
 *@version 1.0
 *@create 2024/6/12 15:47
 */

/**
 * 响应状态枚举
 */
public enum ResponseStatus {
    /**
     * 内置状态
     */
    OK(20000,"操作成功"),
    INTERNAL_ERROR(500000,"系统错误"),
    BUSINESS_ERROR(500001,"业务错误"),
    LOGIN_ERROR(500002,"账号或密码错误"),
    NO_DATA_ERROR(500003,"没有找到数据"),
    PARAM_ERROR(500004,"参数格式错误"),
    AUTH_ERROR(401,"没有权限,需要登录");

    //响应代码
    private Integer code;
    //响应消息
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseStatus(Integer status, String message) {
        this.code = status;
        this.message = message;
    }
}