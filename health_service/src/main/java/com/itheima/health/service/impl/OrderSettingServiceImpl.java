package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/9
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约设置
     *
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void addBatch(List<OrderSetting> orderSettingList) {
        //- 判断List<Ordersetting>不为空
        if(!CollectionUtils.isEmpty(orderSettingList)) {
            //- 遍历导入的预约设置信息List<Ordersetting>
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (OrderSetting os : orderSettingList) {
                //- 通过预约的日期来查询预约设置表，看这个日期的设置信息有没有
                OrderSetting osInDB = orderSettingDao.findByOrderDate(os.getOrderDate());
                //- 没有预约设置(表中没有这个日期的记录)
                if(null == osInDB) {
                    //  - 调用dao插入数据
                    orderSettingDao.add(os);
                }else {
                    //- 有预约设置(表中有这个日期的记录)
                    //  - 判断已预约人数是否大于要更新的最大预约数
                    // 已预约人数
                    int reservations = osInDB.getReservations();
                    //要更新的最大预约数
                    int number = os.getNumber();
                    if(reservations > number) {
                        //  - 大于则要报错，接口方法 异常声明
                        throw new MyException(sdf.format(os.getOrderDate()) + ": 最大预约数不能小于已预约人数");
                    } else {
                        //  - 小于，则可以更新最大预约数
                        orderSettingDao.updateNumber(os);
                    }
                }
            }
        }
    }

    /**
     * 按月查询预约设置信息
     * @param month 2021-01
     * @return
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        month+="%";
        return orderSettingDao.getOrderSettingByMonth(month);
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) throws MyException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //- 通过预约的日期来查询预约设置表，看这个日期的设置信息有没有
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //- 没有预约设置(表中没有这个日期的记录)
        if(null == osInDB) {
            //  - 调用dao插入数据
            orderSettingDao.add(orderSetting);
        }else {
            //- 有预约设置(表中有这个日期的记录)
            //  - 判断已预约人数是否大于要更新的最大预约数
            // 已预约人数
            int reservations = osInDB.getReservations();
            //要更新的最大预约数
            int number = orderSetting.getNumber();
            if(reservations > number) {
                //  - 大于则要报错，接口方法 异常声明
                throw new MyException(sdf.format(orderSetting.getOrderDate()) + ": 最大预约数不能小于已预约人数");
            } else {
                //  - 小于，则可以更新最大预约数
                orderSettingDao.updateNumber(orderSetting);
            }
        }
    }
}
