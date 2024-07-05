package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jforgame.admin.system.vo.SysUserVo;
import lombok.Data;



@Entity(name = "sys_user")
@Data
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String salt;

    @Column
    private String email;

    @Column
    private String mobile;

    private Byte status;

    @Column(name = "dept_id")
    private Long deptId;

    public SysUserVo simpleView() {
        SysUserVo vo = new SysUserVo();
        vo.setId(getId());
        vo.setName(getName());
        vo.setEmail(getEmail());
        vo.setMobile(getMobile());
        vo.setDeptId(getDeptId());
        return vo;
    }

}
