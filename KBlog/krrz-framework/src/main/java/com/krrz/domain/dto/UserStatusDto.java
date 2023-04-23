package com.krrz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDto {
    private Long userId;

    //角色状态（0正常 1停用）
    private String status;
}
