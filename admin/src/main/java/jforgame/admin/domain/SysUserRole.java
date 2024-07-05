package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jforgame.admin.system.vo.SysUserRoleVo;
import lombok.Data;

import jakarta.persistence.Id;

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
