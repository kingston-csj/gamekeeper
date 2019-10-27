package com.kingston.jforgame.admin.payorder.dao;

import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import com.kingston.jforgame.admin.payorder.domain.PayOrderGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/25 17:19
 */
public interface PayOrderDao extends JpaRepository<PayOrder, String> , JpaSpecificationExecutor<PayOrder> {


    // TODO 优雅的实现？？
    @Query(nativeQuery =true, value="SELECT sum(money) as money,channelCode "

            + " FROM t_payinfo"

            + " WHERE createTime >= ? "
            + " AND createTime <= ? "

            + " GROUP BY channelCode")
    List<Object> queryOrderStatistics(Date fromDate, Date toDate);

}
