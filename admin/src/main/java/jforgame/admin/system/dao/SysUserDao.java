package jforgame.admin.system.dao;

import jforgame.admin.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserDao extends JpaRepository<SysUser, Long> {

    SysUser findByName(String name);
}
