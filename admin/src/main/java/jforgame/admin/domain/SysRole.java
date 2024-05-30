package jforgame.admin.domain;

import jforgame.admin.system.vo.SysRoleVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="sys_role")
@Data
public class SysRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String remark;

    public SysRoleVo simpleView() {
        SysRoleVo vo = new SysRoleVo();
        vo.setId(getId());
        vo.setName(getName());
        vo.setRemark(getRemark());
        return vo;
    }

}
