package jforgame.admin.payorder.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jforgame.admin.channel.domain.Channel;
import jforgame.admin.channel.service.ChannelService;
import jforgame.admin.payorder.dao.PayOrderDao;
import jforgame.admin.payorder.domain.PayOrder;
import jforgame.admin.payorder.domain.PayOrderGroup;
import jforgame.admin.payorder.io.PayChannelStatistics;
import jforgame.admin.system.model.RoleKinds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayOrderService {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private PayOrderDao payOrderDao;

    /**
     * 查看订单列表详情
     *
     * @param userName
     * @param startTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    public Page<PayOrder> queryOrdersDetail(String userName, long startTime, long endTime, int page, int pageSize) {
        // 父渠道可以查询所有子渠道的订单
        Channel channel = channelService.findChannelByUserName(userName);
        List<String> children = Collections.emptyList();
        boolean isAdmin = RoleKinds.ADMIN.equalsIgnoreCase(userName);
        if (channel != null) {
            children = channelService.queryChildChannel(channel.getChannelNo());
        }
        // 超级管理员可以查看所有渠道
        if (children.isEmpty() && !isAdmin) {
            return null;
        }
        page = Math.abs(page);
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest =  PageRequest.of(page - 1, pageSize);
        return payOrderDao.findAll(createQuerySpecification(startTime, endTime, children), pageRequest);
    }

    private Specification<PayOrder> createQuerySpecification(long startTime, long endTime, List<String> channels) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), new Date(startTime)));
            predicateList.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), new Date(endTime)));
            if (!channels.isEmpty()) {
                Path<Object> path = root.get("channelCode");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String channel : channels) {
                    in.value(channel);
                }
                predicateList.add(cb.and(cb.and(in)));
            }

            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }

    /**
     * 渠道金额统计
     * @param userName
     * @param startTime
     * @param endTime
     * @return
     */
    public PayChannelStatistics queryOrderStatistics(String userName, long startTime, long endTime) {
        // 父渠道可以查询所有子渠道的订单
        Channel channel = channelService.findChannelByUserName(userName);
        List<String> children = Collections.emptyList();
        if (channel != null) {
            children = channelService.queryChildChannel(channel.getChannelNo());
        }
        PayChannelStatistics result = new PayChannelStatistics();
        // 超级管理员可以查看所有渠道
        boolean isAdmin = RoleKinds.ADMIN.equalsIgnoreCase(userName);
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
            String channelNo =  params[1].toString().toUpperCase().trim();
            if (!isAdmin && !children.contains(channelNo)) {
                continue;
            }
            assert channel != null;
            int prev = channelMap.getOrDefault(channel.getChannelNo(), 0);
            channelMap.put(channelNo, prev + money);
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
