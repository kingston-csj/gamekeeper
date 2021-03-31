package jforgame.admin.domain;

import jforgame.admin.system.vo.SysMenuVo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity(name="sys_menu")
public class SysMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="parent_id")
    private Long parentId;

    @Column
    private String name;

    @Column
    private String url;

    @Column
    private String perms;

    @Column
    private Integer type;

    @Column
    private String icon;

    @Column(name="order_num")
    private Integer orderNum;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<SysMenu> getChildren() {
        return new ArrayList<>();
    }

    public void setChildren(List<SysMenu> children) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SysMenuVo simpleView() {
        SysMenuVo vo = new SysMenuVo();
        vo.setId(getId());
        vo.setIcon(getIcon());
        vo.setName(getName());
        vo.setOrderNum(getOrderNum());
        vo.setType(getType());
        vo.setUrl(getUrl());
        vo.setPerms(getPerms());

        return vo;
    }
}
