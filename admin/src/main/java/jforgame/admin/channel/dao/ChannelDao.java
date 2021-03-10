package jforgame.admin.channel.dao;

import jforgame.admin.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Kinson
 * @Date: 2019/10/24 20:04
 */
public interface ChannelDao extends JpaRepository<Channel, String> {


}
