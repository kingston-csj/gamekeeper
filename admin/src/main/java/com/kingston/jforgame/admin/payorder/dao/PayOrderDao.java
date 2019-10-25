package com.kingston.jforgame.admin.payorder.dao;

import com.kingston.jforgame.admin.payorder.domain.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/25 17:19
 */
public interface PayOrderDao extends JpaRepository<PayOrder, String> {

    List<PayOrder> findByChannelCodeIn(List<String> channels);
}
