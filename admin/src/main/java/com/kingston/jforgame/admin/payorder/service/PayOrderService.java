package com.kingston.jforgame.admin.payorder.service;

import com.kingston.jforgame.admin.channel.service.ChannelService;
import com.kingston.jforgame.admin.payorder.dao.PayOrderDao;
import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/25 17:08
 */
@Service
public class PayOrderService {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private PayOrderDao payOrderDao;

    public void queryOrderList(String channelId) {
        // 父渠道可以查询所有子渠道的订单
        List<String> children = channelService.queryChildChannel(channelId);
        List<PayOrder> orders = payOrderDao.findByChannelCodeIn(children);
        System.out.println(orders.size());
    }

    @PostConstruct
    private void init() {
        queryOrderList("DJ550");
    }
}
