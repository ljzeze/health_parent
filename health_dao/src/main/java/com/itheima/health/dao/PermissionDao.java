package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionDao {
    List<Permission> findAll();

    void add(Permission permission);

    Page<Permission> findByCondition(String queryString);

    Permission findById(int id);

    void update(Permission permission);

    int findCountByPermissionId(int id);

    void deleteById(int id);
}
