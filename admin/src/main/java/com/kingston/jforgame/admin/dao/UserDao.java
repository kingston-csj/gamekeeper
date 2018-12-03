package com.kingston.jforgame.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingston.jforgame.admin.domain.User;

public interface UserDao extends JpaRepository<User, Long> {
	
	User findById(Long userId);
	
	User findByUserName(String name);

}
