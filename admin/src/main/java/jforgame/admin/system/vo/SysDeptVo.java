package jforgame.admin.system.vo;

import java.util.List;

public class SysDeptVo {

    private Long id;

    private String name;
    private Long parentId;
    private Integer orderNum;

    private int level;

    private String parentName;

    private List<SysDeptVo> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysDeptVo> getChildren() {
        return children;
    }

    public void setChildren(List<SysDeptVo> children) {
        this.children = children;
    }
}
