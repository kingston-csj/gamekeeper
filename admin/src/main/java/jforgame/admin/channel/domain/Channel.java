package jforgame.admin.channel.domain;

import jforgame.admin.domain.SysUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: Kinson
 * @Date: 2019/10/24 20:02
 */
@Entity(name = "t_channel")
public class Channel {

    @Id
    /**
     * {@link SysUser#getId()}
     */
    private Long id;

    @Column
    private String channelNo;

    @Column
    private String parentChannel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParentChannel(String parentChannel) {
        this.parentChannel = parentChannel;
    }

    public String getChannelNo() {
        // 不区分大小写
        return channelNo.toUpperCase();
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getParentChannel() {
        return parentChannel;
    }

    public String getUsername() {
        return channelNo;
    }

}
