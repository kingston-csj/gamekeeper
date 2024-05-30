package jforgame.admin.domain;

import jforgame.admin.system.vo.SysUserRoleVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="sys_user_role")
@Data
public class SysUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    public SysUserRoleVo simpleView() {
        SysUserRoleVo vo = new SysUserRoleVo();
        vo.setId(getId());
        vo.setRoleId(getRoleId());
        vo.setUserId(getUserId());
        return vo;
    }
}
