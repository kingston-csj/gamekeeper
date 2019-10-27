package com.kingston.jforgame.admin.payorder.vo;

import java.util.List;

public class PayOrderStatistics {

    private List<PayOrderVo> orders;

    private int totalRecord;

    public List<PayOrderVo> getOrders() {
        return orders;
    }

    public void setOrders(List<PayOrderVo> orders) {
        this.orders = orders;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
}
