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
public class ListLinkDto {

    private String name;

    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    private String status;
    

}
