package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService{

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    /**
     * 添加权限
     * @param permission
     */
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    /**
     * 检查项的分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Permission> findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 条件查询
        if(StringUtils.isNotEmpty(queryPageBean.getQueryString())){
            // 有查询条件， 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // page extends arrayList
        Page<Permission> page = permissionDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Permission> pageResult = new PageResult<Permission>(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public Permission findById(int id) {
        return permissionDao.findById(id);
    }

    /**
     * 修改检查项
     * @param permission
     */
    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
    }

    /**
     * 通过id删除
     * @param id
     * @throws MyException
     */
    @Override
    public void deleteById(int id){
        //  统计使用了这个id的个数
        int count =permissionDao.findCountByPermissionId(id);
        if(count > 0){
            throw new MyException("该权限被使用了，不能删除!");
        }
        // 删除
        permissionDao.deleteById(id);
    }
}
