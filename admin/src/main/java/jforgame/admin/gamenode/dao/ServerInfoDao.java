package jforgame.admin.gamenode.dao;

import jforgame.admin.domain.ServerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerInfoDao extends JpaRepository<ServerInfo, Integer> {

}
