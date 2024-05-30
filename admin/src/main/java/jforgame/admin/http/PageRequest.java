package jforgame.admin.http;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PageRequest {
    /**
     * 当前页码
     */
    private int pageNum = 1;
    /**
     * 每页数量
     */
    private int pageSize = 10;
    /**
     * 每页数量
     */
    private Map<String, ColumnFilter> columnFilters = new HashMap<String, ColumnFilter>();

    public ColumnFilter getColumnFilter(String name) {
        return columnFilters.get(name);
    }
}