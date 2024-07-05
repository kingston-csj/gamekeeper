package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jforgame.admin.system.vo.SysRoleVo;
import lombok.Data;

import jakarta.persistence.Id;

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
