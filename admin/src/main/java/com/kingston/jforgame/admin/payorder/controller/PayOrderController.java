package com.kingston.jforgame.admin.payorder.controller;

import com.kingston.jforgame.admin.gamenode.vo.ServerNodeInfoList;
import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import com.kingston.jforgame.admin.payorder.service.PayOrderService;
import com.kingston.jforgame.admin.payorder.vo.PayOrderStatistics;
import com.kingston.jforgame.admin.payorder.vo.PayOrderVo;
import com.kingston.jforgame.admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Kingston
 * @Date: 2019/10/26 20:08
 */
@RestController
@RequestMapping("/channel")
public class PayOrderController {

    @Autowired
    private PayOrderService orderService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public @ResponseBody
    PayOrderStatistics getArticleByState(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer count,
                                         @RequestParam(value = "selectFrom") Long selectFrom,
                                         @RequestParam(value = "selectTo") Long selectTo) {
        String channelNo = userService.getCurrentUser();
        List<PayOrder> orders = orderService.queryOrderList(channelNo, selectFrom, selectTo, page, count);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:MM:ss");
        List<PayOrderVo> vos = new ArrayList<>(orders.size());

        PayOrderStatistics result = new PayOrderStatistics();
        int moneySum = 0;

        Map<String, Integer> childrenSum = new HashMap<>();
        for (PayOrder order : orders) {
            PayOrderVo vo = new PayOrderVo();
            vo.setAccount(order.getChannelCode());
            vo.setChannel(order.getChannelCode());
            vo.setId(order.getTradeNo());
            vo.setMoney(order.getMoney());
            vo.setTime(format.format(order.getCreateTime()));
            vos.add(vo);

            int prev = childrenSum.getOrDefault(order.getChannelCode(), 0);
            childrenSum.put(order.getChannelCode(), prev + vo.getMoney());

            moneySum += vo.getMoney();
        }
        result.setOrderVos(vos);

        if (moneySum > 0) {
            result.setMoneySum(moneySum + " " + childrenSum.toString());
        }

        return result;
    }

}
