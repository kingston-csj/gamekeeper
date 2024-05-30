package jforgame.admin.payorder.io;

import jforgame.admin.payorder.domain.PayOrderGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PayChannelStatistics {

    private List<PayOrderGroup> orderGroups;

    private int moneySum;

}
