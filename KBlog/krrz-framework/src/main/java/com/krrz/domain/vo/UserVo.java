package com.krrz.domain.vo;

import com.krrz.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private List roleIds;
    private List roles;
    private UserVoVo user;
}
