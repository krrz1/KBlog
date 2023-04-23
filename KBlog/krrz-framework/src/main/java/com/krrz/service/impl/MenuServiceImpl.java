package com.krrz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.Menu;
import com.krrz.domain.entity.RoleMenu;
import com.krrz.domain.vo.MenuListVo;
import com.krrz.domain.vo.MenuTreeBackVo;
import com.krrz.domain.vo.MenuTreeVo;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.mapper.MenuMapper;
import com.krrz.service.MenuService;
import com.krrz.service.RoleMenuService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.IsAdminUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-11-13 15:05:28
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理院 返回所有权限
        if(IsAdminUtils.isAdmin(id)){
            LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(wrapper);
            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper=getBaseMapper();
        List<Menu> menus;
        //判断是否是管理员
        if(IsAdminUtils.isAdmin(userId)){
            //如果是查询道所有的Menu
            menus=menuMapper.selectAllRouterMenu();
        }else{
            //否则查询当前用户所具有的RouterMenu
            menus=menuMapper.selectRouterMenuByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单，然后去找他们的子菜单设置道children
        List<Menu> menuTree=builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public List<MenuListVo> menuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
        if(status!=null)
            wrapper.like(Menu::getStatus,status);
        if(menuName!=null)
            wrapper.like(Menu::getMenuName,menuName);
        List<Menu> menuList = list(wrapper);
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(menuList, MenuListVo.class);
        return menuListVos;
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenu(Long id) {
        Menu byId = getById(id);
        MenuListVo menuListVo = BeanCopyUtils.copyBean(byId, MenuListVo.class);
        return ResponseResult.okResult(menuListVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //在父菜单选择了自己时 返回报错信息
        if(menu.getParentId().equals(menu.getId()))
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"修改菜单发生错误，上级菜单不能选择自己");
        //正常修改
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        //删除菜单时如果有子菜单不允许删除
        LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        List<Menu> list = list(wrapper);
        if(!list.isEmpty()) return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"存在子菜单不允许删除");
        //正常删除
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<MenuTreeVo> treeselect() {
        //查询所有菜单
        LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = getBaseMapper().selectList(wrapper);
        //转换成Vo防止冗余
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);
        for(int i=0;i<menuTreeVos.size();i++)
            menuTreeVos.get(i).setLabel(menus.get(i).getMenuName());
        List<MenuTreeVo> menuTreeVos1 = builderMenuTreeVo(menuTreeVos, 0L);
        return menuTreeVos1;
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        //加载对应角色菜单列表树接口
        List<MenuTreeVo> treeselect = treeselect();
        //查询角色所关联菜单权限id列表
        LambdaQueryWrapper<RoleMenu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> list = roleMenuService.list(wrapper);
        List<Long> menuId=new ArrayList<>();
        for(RoleMenu roleMenu:list){
            menuId.add(roleMenu.getMenuId());
        }
        //封装成MenoTreeVo
        return ResponseResult.okResult(new MenuTreeBackVo(treeselect,menuId));
    }

    private List<MenuTreeVo> builderMenuTreeVo(List<MenuTreeVo> menuTreeVos,Long parentId){
        List<MenuTreeVo> result=new ArrayList<>();
        for(MenuTreeVo menuTreeVo:menuTreeVos){
            if(menuTreeVo.getParentId().equals(parentId)){
                List<MenuTreeVo> children=builderMenuTreeVo(menuTreeVos,menuTreeVo.getId());
                menuTreeVo.setChildren(children);
                result.add(menuTreeVo);
            }
        }
        return result;
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menusTree=new ArrayList<>();
        for (Menu menu : menus) {
            if(menu.getParentId().equals(parentId)){
                List<Menu> children=builderMenuTree(menus,menu.getId());
                menu.setChildren(children);
                menusTree.add(menu);
            }
        }
        return  menusTree;

//        List<Menu> menusTree = menus.stream()
//                .filter(menu -> menu.getParentId().equals(parentId))
//                .map(menu -> menu.setChildren(getChildren(menu, menus)))
//                .collect(Collectors.toList());
//        return menusTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        //从 menus中获取 children
        List<Menu> childrenList=menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

