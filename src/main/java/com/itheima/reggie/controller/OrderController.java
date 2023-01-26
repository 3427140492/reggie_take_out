package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 订单状态
     * @param orders
     * @return
     */
    @PutMapping()
    public R<String> editOrder(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);

        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(orders.getId() != null, Orders::getId, orders.getId());
        updateWrapper.set(orders.getStatus() != null, Orders::getStatus, orders.getStatus());

        orderService.update(null, updateWrapper);

        return R.success("订单状态修改成功");
    }

    /**
     * 订单信息分页查询
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(String number, String beginTime, String endTime){
        Page<Orders> pageInfo = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null,Orders::getNumber,number);
        queryWrapper.ge(beginTime != null,Orders::getOrderTime,beginTime);
        queryWrapper.le(endTime != null,Orders::getCheckoutTime,endTime);

        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @return
     */
    @GetMapping("/list")
    public R<Page> list(){
        Page<Orders> pageInfo = new Page<>();

        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null,Orders::getUserId,userId);

        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
}