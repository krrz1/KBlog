package com.krrz.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleStatusDto {
    private Long roleId;

    //角色状态（0正常 1停用）
    private String status;
}
