package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/8
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //1. 获取源文件名
        String originalFilename = imgFile.getOriginalFilename();
        //2. 截取后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //3. 生成唯一id,
        String uniqueId = UUID.randomUUID().toString();
        //4. 拼接唯一文件名
        String filename = uniqueId + suffix;
        //5. 调用7牛工具上传图片
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);
            //6. 构建返回的数据
            /*
             {
                imgName: 图片名 , 补全formData.img
                domain: 七牛的域名 图片回显imageUrl = domain+图片名
            }
             */
            Map<String,String> map = new HashMap<String,String>(2);
            map.put("imgName", filename);
            map.put("domain",QiNiuUtils.DOMAIN);
            //7. 放到result里，再返回给页面
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            log.error("上传文件失败了",e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务添加套餐
        Integer setmealId = setmealService.add(setmeal, checkgroupIds);
        // 添加成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        jedis.zadd(key, currentTimeMillis.doubleValue(),setmealId+"|1|" + currentTimeMillis);
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> setmealPageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealPageResult);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 套餐信息
        Setmeal setmeal = setmealService.findById(id);
        // 构建前端需要的数据, 还要有域名
        Map<String,Object> map = new HashMap<String,Object>(2);
        map.put("setmeal",setmeal);
        map.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    /**
     * 查询选中的检查组id集合
     */
    @GetMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,checkgroupIds);
    }

    /**
     * 修改套餐
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务修改套餐
        setmealService.update(setmeal,checkgroupIds);
        // 添加成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        jedis.zadd(key, currentTimeMillis.doubleValue(),setmeal.getId()+"|1|" + currentTimeMillis);
        jedis.close();
        return new Result(true, "编辑套餐成功");
    }

    /**
     * 通过id删除套餐
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        setmealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        jedis.zadd(key, currentTimeMillis.doubleValue(),id+"|0|" + currentTimeMillis);
        jedis.close();
        return new Result(true, "删除套餐成功");
    }
}
