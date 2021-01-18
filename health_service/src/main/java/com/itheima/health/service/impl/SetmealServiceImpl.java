package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/8
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 添加套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public Integer add(Setmeal setmeal, Integer[] checkgroupIds) {
        //- 先 添加套餐
        setmealDao.add(setmeal);
        //- 获取套餐的id
        Integer setmealId = setmeal.getId();
        //- 遍历checkgroupIds数组
        if(null != checkgroupIds){
            //- 添加套餐与检查组的关系
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }
        //- 事务控制
        return setmealId;
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 条件查询
        if(StringUtils.isNotEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<Setmeal> setmealPage = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(setmealPage.getTotal(),setmealPage.getResult());
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 查询选中的检查组id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(int id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 更新套餐
        setmealDao.update(setmeal);
        // 删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 遍历添加 新关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
        // 事务控制
    }

    /**
     * 通过id删除套餐
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        // 先判断 是否被订单使用了
        int count = setmealDao.findCountBySetmealId(id);
        // 使用了，要报错，接口方法 异常声明
        if(count > 0){
            throw new MyException("该套餐被订单使用了，不能删除");
        }
        // 没使用，则要先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 再删除套餐
        setmealDao.deleteById(id);
    }

    /**
     * 查询套餐的所有图片
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

    /**
     * 查询所有的套餐
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 通过id查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }

    @Override
    public Setmeal findDetailById3(int id) {
        // 查询套餐信息
        Setmeal setmeal = setmealDao.findById(id);
        // 查询套餐下的检查组
        List<CheckGroup> checkGroups = setmealDao.findCheckGroupListBySetmealId(id);
        if(null != checkGroups){
            for (CheckGroup checkGroup : checkGroups) {
                // 通过检查组id检查检查项列表
                List<CheckItem> checkItems = setmealDao.findCheckItemByCheckGroupId(checkGroup.getId());
                // 设置这个检查组下所拥有的检查项
                checkGroup.setCheckItems(checkItems);
            }
            //设置套餐下的所拥有的检查组
            setmeal.setCheckGroups(checkGroups);
        }
        return setmeal;
    }

    /**
     * 统计每个套餐的预约数
     * @return
     */
    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }
}
