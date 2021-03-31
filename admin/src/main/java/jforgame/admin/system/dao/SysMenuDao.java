package jforgame.admin.system.dao;

import jforgame.admin.domain.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysMenuDao extends JpaRepository<SysMenu, Long> {


    @Query(value = " select m.* from sys_menu m, sys_user u, sys_user_role ur, sys_role_menu rm" +
            "  where u.name = ?1 and u.id = ur.user_id " +
            "  and ur.role_id = rm.role_id and rm.menu_id = m.id", nativeQuery = true)
    List<SysMenu> findByUserName(String userName);

    @Query(value = " select m.* from sys_menu m, sys_user u, sys_user_role ur, sys_role_menu rm" +
            "  where rm.role_id = ?1 and u.id = ur.user_id " +
            "  and ur.role_id = rm.role_id and rm.menu_id = m.id", nativeQuery = true)
    List<SysMenu> findByRoleId(Long roleId);

}
