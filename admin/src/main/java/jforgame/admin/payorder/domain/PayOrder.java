package jforgame.admin.payorder.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name="t_payorder")
public class PayOrder {

    @Id
    @Column
    private String tradeNo;

    @Column
    private String channelCode;

    @Column
    private int money;

    private Date createTime;

}
