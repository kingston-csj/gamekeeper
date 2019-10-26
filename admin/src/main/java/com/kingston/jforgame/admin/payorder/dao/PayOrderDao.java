package com.kingston.jforgame.admin.payorder.dao;

import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/25 17:19
 */
public interface PayOrderDao extends JpaRepository<PayOrder, String> , JpaSpecificationExecutor<PayOrder> {

    List<PayOrder> findByChannelCodeIn(List<String> channels);
}
