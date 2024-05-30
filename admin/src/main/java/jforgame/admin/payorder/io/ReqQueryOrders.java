package jforgame.admin.payorder.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqQueryOrders {

    private int page = 1;

    private int pageSize = 10;

    private Long selectFrom;

    private Long selectTo;
}
