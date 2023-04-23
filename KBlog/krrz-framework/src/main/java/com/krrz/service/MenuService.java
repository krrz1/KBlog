package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.Menu;
import com.krrz.domain.vo.MenuListVo;
import com.krrz.domain.vo.MenuTreeVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-11-13 15:05:28
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<MenuListVo> menuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenu(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    List<MenuTreeVo> treeselect();

    ResponseResult roleMenuTreeselect(Long id);
}
