package jforgame.admin.payorder.dao;

import jforgame.admin.payorder.domain.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PayOrderDao extends JpaRepository<PayOrder, String> , JpaSpecificationExecutor<PayOrder> {


    // TODO 优雅的实现？？
    @Query(nativeQuery =true, value="SELECT sum(money) as money,channelCode "

            + " FROM t_payorder"

            + " WHERE createTime >= ? "
            + " AND createTime <= ? "

            + " GROUP BY channelCode")
    List<Object> queryOrderStatistics(Date fromDate, Date toDate);

}
