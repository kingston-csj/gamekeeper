package jforgame.admin.http;

public class ColumnFilter {

    /**
     * 过滤列名
     */
    private String name;
    /**
     * 查询的值
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}