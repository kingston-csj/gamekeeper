package jforgame.admin.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnFilter {

    /**
     * 过滤列名
     */
    private String name;
    /**
     * 查询的值
     */
    private String value;


}