package jforgame.admin.user.dao;

import jforgame.admin.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesDao extends JpaRepository<Roles, Long> {
	

}
