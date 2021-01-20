package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();

    void add(Permission permission);

    PageResult<Permission> findPage(QueryPageBean queryPageBean);

    Permission findById(int id);

    void update(Permission permission);

    void deleteById(int id) throws MyException;
}
