package com.krrz.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDto {
    //分类名
    private String name;
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;

}
