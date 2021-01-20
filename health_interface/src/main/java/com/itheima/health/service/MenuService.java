package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> children();

    /**
     * 添加菜单成功
     * @param menu
     */
    void add(Menu menu,Integer[] roleIds);

    /**
     * 分页查询菜单
     * @param queryPageBean
     * @return
     */
    PageResult<Menu> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    Menu findById(int id);

    /**
     * 修改菜单成功
     * @param menu
     */
    void update(Menu menu,Integer[] roleIds);

    /**
     * 删除菜单
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据菜单id查询角色id成功
     * @param id
     * @return
     */
    List<Integer> findRoleIdByMenuId(int id);
}
