package com.kingston.jforgame.admin.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingston.jforgame.admin.domain.Roles;

public interface RolesDao extends JpaRepository<Roles, Long> {
	

}
