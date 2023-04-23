package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.Menu;
import com.krrz.domain.vo.MenuListVo;
import com.krrz.domain.vo.MenuTreeVo;
import com.krrz.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.ResolverStyle;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @PreAuthorize("@ps.hasPermission('system:menu:list')")
    @GetMapping("/list")
    public ResponseResult menuList(@RequestParam(required = false,name = "status")String status,@RequestParam(required = false,name = "menuName")String menuName){
        List<MenuListVo> menuListVos=menuService.menuList(status,menuName);
        return ResponseResult.okResult(menuListVos);
    }
    @PreAuthorize("@ps.hasPermission('system:menu:add')")
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    //修改菜单时返回给前端的数据
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable("id")Long id){
        return menuService.getMenu(id);
    }
    //更新菜单
    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    //删除菜单
    @PreAuthorize("@ps.hasPermission('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId")Long id){
        return menuService.deleteMenu(id);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        List<MenuTreeVo> treeselect = menuService.treeselect();
        return ResponseResult.okResult(treeselect);
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id")Long id){

        return menuService.roleMenuTreeselect(id);
    }
}
