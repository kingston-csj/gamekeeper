package com.kingston.jforgame.admin.payorder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: Kingston
 * @Date: 2019/10/25 17:17
 */
@Entity(name="t_payinfo")
public class PayOrder {


    @Id
    @Column
    private String tradeNo;

    @Column
    private String channelCode;

    @Column
    private int money;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}