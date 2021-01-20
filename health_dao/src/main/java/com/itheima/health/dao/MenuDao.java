package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> children();

    /**
     * 添加菜单
     * @param menu
     */
    void add(Menu menu);

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    Menu findById(int id);

    /**
     * 分页查询菜单
     * @param queryString
     * @return
     */
    Page<Menu> findByCondition(String queryString);

    /**
     * 修改菜单
     * @param menu
     */
    void update(Menu menu);

    /**
     * 查询是否有使用该id
     * @param id
     * @return
     */
    int findCountByMenuId(int id);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 添加菜单跟角色的关联
     * @param menuId
     * @param roleId
     */
    void addMenuRole(@Param("menuId") Integer menuId,@Param("roleId") Integer roleId);

    /**
     * 根据菜单id查询角色id
     * @param id
     * @return
     */
    List<Integer> findRoleIdByMenuId(int id);

    /**
     * 删除角色与菜单的旧关联
     * @param id
     */
    void deleteRoleMenu(Integer id);
}
