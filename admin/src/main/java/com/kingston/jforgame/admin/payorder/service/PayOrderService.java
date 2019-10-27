package com.kingston.jforgame.admin.payorder.service;

import com.kingston.jforgame.admin.channel.service.ChannelService;
import com.kingston.jforgame.admin.payorder.dao.PayOrderDao;
import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import com.kingston.jforgame.admin.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    public List<PayOrder> queryOrderList(String channelId, long startTime, long endTime, int page, int pageSize) {
        // 父渠道可以查询所有子渠道的订单
        List<String> children = channelService.queryChildChannel(channelId);
        // 超级管理员可以查看所有渠道
        if (children.size() <= 0 && !SecurityUtils.hasAuth("ADMIN")) {
            return new ArrayList<>();
        }
        Pageable pageRequest = new PageRequest(page - 1, Integer.MAX_VALUE);
        Page<PayOrder> orders = payOrderDao.findAll(createSpecification(startTime, endTime, children), pageRequest);
        return orders.getContent();
    }

    private Specification<PayOrder> createSpecification(long startTime, long endTime, List<String> channels) {
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

}
