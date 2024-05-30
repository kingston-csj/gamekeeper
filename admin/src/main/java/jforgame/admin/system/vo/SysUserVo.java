package jforgame.admin.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysUserVo {

    private Long id;

    private String name;

    private String password;

    private String salt;

    private String email;

    private String mobile;

    private Byte status;

    private Long deptId;

    private List<SysUserRoleVo> userRoleVos;

    private String roleNames;

}
