package jforgame.admin.domain;

import jforgame.admin.system.vo.SysDictVo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "sys_dict")
public class SysDict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String value;

    @Column
    private String label;

    @Column
    private String type;

    @Column
    private String description;
    @Column
    private Long sort;
    @Column
    private String remarks;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SysDictVo simpleView() {
        SysDictVo vo = new SysDictVo();
        vo.setId(getId());
        vo.setDescription(getDescription());
        vo.setLabel(getLabel());
        vo.setRemarks(getRemarks());
        vo.setType(getType());
        vo.setSort(getSort());
        vo.setValue(getValue());
        return vo;
    }
}


