package com.kingston.jforgame.admin.channel.dao;

import com.kingston.jforgame.admin.channel.domain.Channel;
import com.kingston.jforgame.admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Kingston
 * @Date: 2019/10/24 20:04
 */
public interface ChannelDao extends JpaRepository<Channel, String> {


}
