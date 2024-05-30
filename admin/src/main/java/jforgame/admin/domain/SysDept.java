package jforgame.admin.domain;

import jforgame.admin.system.vo.SysDeptVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "sys_dept")
@Data
public class SysDept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "order_num")
    private Integer orderNum;

    public SysDeptVo simpleView() {
        SysDeptVo vo = new SysDeptVo();
        vo.setOrderNum(getOrderNum());
        vo.setName(getName());
        vo.setId(getId());
        vo.setParentId(getParentId());

        return vo;
    }

}
