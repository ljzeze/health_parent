package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/6
 */
public interface CheckGroupService {

    /**
     * 添加检查组
     * @param checkgroup
     * @param checkitemIds
     */
    void add(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询检查组
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 通过检查组id查询选中的检查项id
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 修改检查组
     * @param checkgroup 检查组信息
     * @param checkitemIds 选中的检查项id数组
     */
    void update(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 通过id删除检查组
     * @param id
     */
    void deleteById(int id) throws MyException;

    /**
     * 查询所有的检查组
     * @return
     */
    List<CheckGroup> findAll();
}
