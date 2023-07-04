package com.krrz.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class DeleteCommentDto {
    private Long id;
    //根评论id
    private Long rootId;

    private Long createBy;
}
