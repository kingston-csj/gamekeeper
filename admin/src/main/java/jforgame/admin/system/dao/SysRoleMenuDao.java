package jforgame.admin.system.dao;

import jforgame.admin.domain.SysRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SysRoleMenuDao extends JpaRepository<SysRoleMenu, Long> {

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("delete from sys_role_menu m where m.roleId = ?1 ")
    void deleteByRoleId(Long roleId);


}
