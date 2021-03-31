package jforgame.admin.system.dao;

import jforgame.admin.domain.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleDao extends JpaRepository<SysUserRole, Long> {

    List<SysUserRole> findByUserId(long userId);
}
