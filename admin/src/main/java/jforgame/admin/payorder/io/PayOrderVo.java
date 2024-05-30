package jforgame.admin.payorder.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOrderVo {

    private String id;

    private String account;

    private String group;

    private String time;

    private int money;

    private String channel;

}
