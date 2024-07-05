package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jforgame.admin.system.vo.SysDictVo;
import lombok.Data;

import jakarta.persistence.Id;
@Entity(name = "sys_dict")
@Data
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


