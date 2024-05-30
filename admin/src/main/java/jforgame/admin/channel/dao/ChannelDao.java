package jforgame.admin.channel.dao;

import jforgame.admin.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelDao extends JpaRepository<Channel, String> {

    @Query(value = " select c.* from t_channel c, sys_user u " +
            "  where c.id = u.id and u.id = c.id " +
            "  and u.name = ?1 ", nativeQuery = true)
    Channel findChannelByUserName(String name);
}
