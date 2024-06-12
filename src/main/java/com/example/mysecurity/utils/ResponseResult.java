package com.example.mysecurity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 响应数据封装对象
 */
@Data
public class ResponseResult<T> {

    /**
     * 访问token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;


    /**
     * 状态信息
     */
    private ResponseStatus status;

    /**
     * 数据
     */
    private T data;

    public ResponseResult(ResponseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseResult(ResponseStatus status,T data,String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.data = data;
    }

    /**
     * 返回成功对象
     * @param data
     * @return
     */
    public static <T>  ResponseResult<T> ok(T data){
        return new ResponseResult<>(ResponseStatus.OK, data);
    }

    public static <T>  ResponseResult<T> okTwo(T data,String accessToken,String refreshToken){
        return new ResponseResult<>(ResponseStatus.OK,data,accessToken,refreshToken);
    }
    /**
     * 返回错误对象
     * @param status
     * @return
     */
    public static ResponseResult<String> error(ResponseStatus status){
        return new ResponseResult<>(status,status.getMessage());
    }

    /**
     * 返回错误对象
     * @param status
     * @return
     */
    public static ResponseResult<String> error(ResponseStatus status, String msg){
        return new ResponseResult<>(status,msg);
    }

    /**
     * 向流中输出结果
     * @param resp
     * @param result
     * @throws IOException
     */
    public static void write(HttpServletResponse resp, ResponseResult result) throws IOException {
        //设置返回数据的格式
        resp.setContentType("application/json;charset=UTF-8");
        //jackson是JSON解析包，ObjectMapper用于解析 writeValueAsString 将Java对象转换为JSON字符串
        String msg = new ObjectMapper().writeValueAsString(result);
        //用流发送给前端
        resp.getWriter().print(msg);
        resp.getWriter().close();
    }

    public static void writeTwo(HttpServletResponse resp, ResponseResult result) throws IOException {
        //设置返回数据的格式
        resp.setContentType("application/json;charset=UTF-8");
        //jackson是JSON解析包，ObjectMapper用于解析 writeValueAsString 将Java对象转换为JSON字符串
        String msg = new ObjectMapper().writeValueAsString(result);
        //用流发送给前端
        resp.getWriter().print(msg);
        resp.getWriter().close();
    }

}