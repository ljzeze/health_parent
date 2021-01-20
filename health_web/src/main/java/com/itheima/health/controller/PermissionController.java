package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        // 调用服务查询
        List<Permission> list = permissionService.findAll();
        // 封装到Result再返回
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,list);
    }

    /**
     * 添加权限
     * @param
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody Permission permission){
        // 调用服务添加
        permissionService.add(permission);
        // 返回操作的结果
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 权限的分页查询
     * @param
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务 分页查询
        PageResult<Permission> pageResult = permissionService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Permission permission = permissionService.findById(id);
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
    }

    /**
     * 修改权限
     * @param
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Permission permission){
        // 调用服务更新
        permissionService.update(permission);
        // 返回操作的结果
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        permissionService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

}
