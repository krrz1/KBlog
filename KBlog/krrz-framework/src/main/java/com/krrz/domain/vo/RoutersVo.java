package com.krrz.domain.vo;

import com.krrz.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVo {
    private List<Menu> menus;

}
