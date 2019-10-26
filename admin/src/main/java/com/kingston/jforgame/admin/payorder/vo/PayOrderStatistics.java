package com.kingston.jforgame.admin.payorder.vo;

import java.util.List;

public class PayOrderStatistics {

    private List<PayOrderVo> orderVos;

    private String moneySum;

    public List<PayOrderVo> getOrderVos() {
        return orderVos;
    }

    public void setOrderVos(List<PayOrderVo> orderVos) {
        this.orderVos = orderVos;
    }

    public String getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(String moneySum) {
        this.moneySum = moneySum;
    }
}
