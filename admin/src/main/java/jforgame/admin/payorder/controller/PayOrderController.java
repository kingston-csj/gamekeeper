package jforgame.admin.payorder.controller;

import jforgame.admin.http.HttpResult;
import jforgame.admin.payorder.domain.PayOrder;
import jforgame.admin.payorder.io.PayOrderStatistics;
import jforgame.admin.payorder.io.PayOrderVo;
import jforgame.admin.payorder.io.ReqQueryOrders;
import jforgame.admin.payorder.service.PayOrderService;
import jforgame.admin.system.service.SysUserService;
import jforgame.commons.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠道充值订单查询
 */
@RestController
@RequestMapping("/pay")
public class PayOrderController {

    @Autowired
    private PayOrderService orderService;
    @Autowired
    private SysUserService userService;

    @GetMapping(value = "/order")
    public HttpResult queryOrdersDetail(ReqQueryOrders req) {
        String channelNo = userService.getCurrentUser();
        Page<PayOrder>  orders = orderService.queryOrdersDetail(channelNo, req.getSelectFrom(), req.getSelectTo(), req.getPage(), req.getPageSize());
        PayOrderStatistics result = new PayOrderStatistics();
        if (orders != null) {
            List<PayOrderVo> vos = new ArrayList<>(orders.getNumberOfElements());
            for (PayOrder order : orders) {
                PayOrderVo vo = new PayOrderVo();
                vo.setAccount(order.getChannelCode());
                vo.setChannel(order.getChannelCode());
                vo.setId(order.getTradeNo());
                vo.setMoney(order.getMoney());
                vo.setTime(DateUtil.format(order.getCreateTime()));
                vos.add(vo);
            }
            result.setOrders(vos);
            result.setTotalRecord((int) orders.getTotalElements());
        }

        return HttpResult.ok(result);
    }

    @GetMapping(value = "/statistics")
    public HttpResult queryChannelStatistics(ReqQueryOrders req) {
        String channelNo = userService.getCurrentUser();
        return HttpResult.ok(orderService.queryOrderStatistics(channelNo, req.getSelectFrom(), req.getSelectTo()));
    }

}
