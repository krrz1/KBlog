package com.krrz.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)//set 返回类型为实体类
public class ListArticleDto {
    //pageNum: 页码
    private Long pageNum;

    //pageSize: 每页条数
    private Long pageSize;

    //title：文章标题
    private String title;

    //summary：文章摘要
    private String summary;
}

