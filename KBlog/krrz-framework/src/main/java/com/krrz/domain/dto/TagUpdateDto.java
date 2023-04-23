package com.krrz.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "修改标签dto")
public class TagUpdateDto {
    @ApiModelProperty(notes = "修改标签的id")
    private Long id;
    private String name;
    private String remark;
}
