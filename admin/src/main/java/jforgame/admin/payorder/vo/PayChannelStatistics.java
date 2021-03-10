package jforgame.admin.payorder.vo;

import jforgame.admin.payorder.domain.PayOrderGroup;

import java.util.List;

public class PayChannelStatistics {

    private List<PayOrderGroup> orderGroups;

    private int moneySum;

    public List<PayOrderGroup> getOrderGroups() {
        return orderGroups;
    }

    public void setOrderGroups(List<PayOrderGroup> orderGroups) {
        this.orderGroups = orderGroups;
    }

    public int getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(int moneySum) {
        this.moneySum = moneySum;
    }
}
