package com.example.mysecurity.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peterlin
 * @version 1.0
 * @description: TODO
 * @date 2022/9/27 10:48 AM
 */
@Data
public class  Result {


    private  Boolean success;


    private  Integer code;


    private  String  msg;


    private Map<String,Object> data=new HashMap<String,Object>();

    private Result(){}//构造方法私有化

    //失败静态方法
    public static Result error(){
        Result result =new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMsg("失败");
        return  result;
    }

    /** 登录已过期 **/
    public static Result loginExpired(){
        Result result =new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.LOGIN_EXPIRED);
        result.setMsg("登录凭证已失效，请重新登录");
        return  result;
    }


    // 无权限
    public static Result denyAccess(){
        Result result =new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.DENYACCESS);
        result.setMsg("失败");
        return  result;
    }

    //成功静态方法
    public static Result success(){
        Result result =new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMsg("成功");
        return  result;
    }

    //为了实现链式编程，使用了return this 当前对象。
    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result msg(String msg){
        this.setMsg(msg);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }
}
