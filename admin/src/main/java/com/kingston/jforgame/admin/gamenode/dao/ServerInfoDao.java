package com.kingston.jforgame.admin.gamenode.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingston.jforgame.admin.domain.ServerInfo;

public interface ServerInfoDao extends JpaRepository<ServerInfo, Integer> {

}
