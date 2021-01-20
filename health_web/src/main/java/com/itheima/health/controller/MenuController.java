package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    /**
     * 查询所有菜单成功
     * @return
     */
    @GetMapping("/children")
    public Result children(){
        List<Menu> menuList = menuService.children();
        return new Result(true, "查询所有菜单成功",menuList);
    }

    /**
     * 添加
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu,Integer[] roleIds){
        menuService.add(menu,roleIds);
        return new Result(true,"添加菜单成功");
    }

    /**
     * 分页查询菜单
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Menu> pageResult = menuService.findPage(queryPageBean);
        return new Result(true,"分页查询菜单成功",pageResult);
    }

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
     @GetMapping("/findById")
    public Result findById(int id){
        Menu menu=menuService.findById(id);
        return new Result(true,"根据id查询菜单成功",menu);
     }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
     @PostMapping("/update")
    public Result update(@RequestBody Menu menu,Integer[] roleIds){
         menuService.update(menu,roleIds);
         return new Result(true,"修改菜单成功");
     }

    /**
     * 根据id删除菜单
     * @param id
     * @return
     */
     @PostMapping("/deleteById")
    public Result deleteById(int id){
         menuService.deleteById(id);
         return new Result(true,"删除菜单成功");
     }

    /**
     * 根据菜单id查询角色id
     * @param id
     * @return
     */
     @GetMapping("/findRoleIdByMenuId")
    public Result findRoleIdByMenuId(int id){
         List<Integer> roleIds = menuService.findRoleIdByMenuId(id);
         return new Result(true,"根据菜单id查询角色id成功");
     }
}
