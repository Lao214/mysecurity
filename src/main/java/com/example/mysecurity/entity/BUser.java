package com.example.mysecurity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 劳威锟
 * @since 2023-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
//(value="BUser对象", description="")
public class BUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //(value = "用户名")
    private String username;

    //(value = "密码")
    private String password;

    //(value = "创建时间")
    private Date createTime;

    //(value = "修改时间")
    private Date updateTime;


}
