package com.kingston.jforgame.admin.payorder.service;

import com.kingston.jforgame.admin.channel.service.ChannelService;
import com.kingston.jforgame.admin.payorder.dao.PayOrderDao;
import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import com.kingston.jforgame.admin.payorder.domain.PayOrderGroup;
import com.kingston.jforgame.admin.payorder.vo.PayChannelStatistics;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.user.model.RoleKInds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 查看订单列表详情
     *
     * @param channelId
     * @param startTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    public Page<PayOrder> queryOrdersDetail(String channelId, long startTime, long endTime, int page, int pageSize) {
        // 父渠道可以查询所有子渠道的订单
        List<String> children = channelService.queryChildChannel(channelId);
        // 超级管理员可以查看所有渠道
        if (children.size() <= 0 && !SecurityUtils.hasAuth(RoleKInds.ADMIN)) {
            return null;
        }
        page = Math.abs(page);
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest = new PageRequest(page - 1, pageSize);
        Page<PayOrder> orders = payOrderDao.findAll(createQuerySpecification(startTime, endTime, children), pageRequest);
        return orders;
    }

    private Specification<PayOrder> createQuerySpecification(long startTime, long endTime, List<String> channels) {
        return new Specification<PayOrder>() {
            @Override
            public Predicate toPredicate(Root<PayOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), new Date(startTime)));
                predicateList.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), new Date(endTime)));


                if (channels.size() > 0) {
                    Path<Object> path = root.get("channelCode");
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    for (String channel : channels) {
                        in.value(channel);
                    }
                    predicateList.add(cb.and(cb.and(in)));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    /**
     * 渠道金额统计
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public PayChannelStatistics queryOrderStatistics(String channelId, long startTime, long endTime) {
        List<String> children = channelService.queryChildChannel(channelId);
        PayChannelStatistics result = new PayChannelStatistics();
        // 超级管理员可以查看所有渠道
        boolean isAdmin = SecurityUtils.hasAuth(RoleKInds.ADMIN);
        if (children.size() <= 0 && !isAdmin) {
            return result;
        }

        List<Object> groups = payOrderDao.queryOrderStatistics(new Date(startTime), new Date(endTime));

        Map<String, Integer> channelMap = new HashMap<>();
        List<PayOrderGroup> orderGroups = new ArrayList<>();
        int moneySum = 0;
        for (Object o : groups) {
            Object[] params = (Object[]) o;
            int money = ((BigDecimal) params[0]).intValue();
            String channel =  params[1].toString().toUpperCase().trim();
            if (!isAdmin && !children.contains(channel)) {
                continue;
            }
            int prev = channelMap.getOrDefault(channel, 0);
            channelMap.put(channel, prev + money);
            moneySum += money;
        }

        for (Map.Entry<String, Integer> entry : channelMap.entrySet()) {
            PayOrderGroup vo = new PayOrderGroup();
            vo.setChannelCode(entry.getKey());
            vo.setMoney(entry.getValue());
            orderGroups.add(vo);
        }

        result.setOrderGroups(orderGroups);
        result.setMoneySum(moneySum);
        return result;
    }


}
