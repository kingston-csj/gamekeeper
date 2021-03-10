package jforgame.admin.payorder.controller;

import jforgame.admin.payorder.domain.PayOrder;
import jforgame.admin.payorder.service.PayOrderService;
import jforgame.admin.payorder.vo.PayChannelStatistics;
import jforgame.admin.payorder.vo.PayOrderStatistics;
import jforgame.admin.payorder.vo.PayOrderVo;
import jforgame.admin.user.service.UserService;
import jforgame.admin.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kinson
 * @Date: 2019/10/26 20:08
 */
@RestController
@RequestMapping("/pay")
public class PayOrderController {

    @Autowired
    private PayOrderService orderService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public @ResponseBody
    PayOrderStatistics queryOrdersDetail(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer count,
                                         @RequestParam(value = "selectFrom") Long selectFrom,
                                         @RequestParam(value = "selectTo") Long selectTo) {
        String channelNo = userService.getCurrentUser();
        Page<PayOrder>  orders = orderService.queryOrdersDetail(channelNo, selectFrom, selectTo, page, count);
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

        return result;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public @ResponseBody
    PayChannelStatistics queryChannelStatistics(
                                       @RequestParam(value = "selectFrom") Long selectFrom,
                                       @RequestParam(value = "selectTo") Long selectTo) {
        String channelNo = userService.getCurrentUser();
        return orderService.queryOrderStatistics(channelNo, selectFrom, selectTo);
    }

}
