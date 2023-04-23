package com.krrz.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoVo {
    //主键@TableId
    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;
    //账号状态（0正常 1停用）
    private String status;
    //邮箱
    private String email;
    //用户性别（0男，1女，2未知）
    private String sex;
    //手机号
    private String phonenumber;
}
