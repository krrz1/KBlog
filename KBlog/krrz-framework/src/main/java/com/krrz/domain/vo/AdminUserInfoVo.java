package com.krrz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserInfoVo {
    private List<String> permissions;
    private List<String > roles;
    private UserInfoVo user;
}
