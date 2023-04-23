package com.krrz.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //用户名
    private String userName;
    //账号状态（0正常 1停用）
    private String status;
    //手机号
    private String phonenumber;

}
