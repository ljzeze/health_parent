package com.itheima.health.service.impl;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService{

   @Autowired
   private MenuDao menuDao;

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> children() {
        return menuDao.children();
    }

    /**
     * 添加菜单
     * @param menu
     */
    @Override
    @Transactional
    public void add(Menu menu,Integer[] roleIds) {
        //添加菜单
        menuDao.add(menu);
        //获取菜单id
        Integer menuId=menu.getId();
        //遍历跟菜单id有关联的角色id
        if (roleIds !=null){
            for (Integer roleId : roleIds) {
                menuDao.addMenuRole(menuId,roleId);
            }
        }
    }

    /**
     * 分页查询菜单
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Menu> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //条件查询,判断条件不为空
        if (StringUtils.isNotEmpty(queryPageBean.getQueryString())){
            //有条件则模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
         //页面分页
        Page<Menu> page = menuDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Menu>(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    @Override
    public Menu findById(int id) {
        return menuDao.findById(id);
    }

    /**
     * 修改菜单
     * @param menu
     */
    @Override
    @Transactional
    public void update(Menu menu,Integer[] roleIds) {
        //修改数据
        menuDao.update(menu);
        //删除旧的关联
        menuDao.deleteRoleMenu(menu.getId());
        //先判断角色id不为空，遍历选中的检查项id的数组
        if (roleIds !=null){
            for (Integer roleId : roleIds) {
                menuDao.addMenuRole(menu.getId(),roleId);
            }
        }
    }

    /**
     * 根据id删除菜单
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        //统计使用这个id的个数
        int count = menuDao.findCountByMenuId(id);
        if (count > 0){
            throw new MyException("该菜单被角色使用，不能删除");
        }
        //最后删除
        menuDao.deleteById(id);
    }

    /**
     * 根据菜单id查询角色id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdByMenuId(int id) {
        return menuDao.findRoleIdByMenuId(id);
    }
}
