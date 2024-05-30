package jforgame.admin.payorder.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PayOrderStatistics {

    private List<PayOrderVo> orders;

    private int totalRecord;

}
