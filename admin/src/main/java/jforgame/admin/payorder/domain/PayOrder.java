package jforgame.admin.payorder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.Id;
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
