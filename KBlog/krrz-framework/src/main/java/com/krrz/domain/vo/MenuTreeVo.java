package com.krrz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeVo {
    //菜单ID@TableId
    private Long id;

    //菜单名称
    private String label;
    //父菜单ID
    private Long parentId;

    private List<MenuTreeVo> children;

}
