package jforgame.admin.channel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jforgame.admin.domain.SysUser;
import lombok.Getter;

import jakarta.persistence.Id;

@Getter
@Entity(name = "t_channel")
public class Channel {

    /**
     * {@link SysUser#getId()}
     */
    @Id
    private Long id;

    @Column
    private String channelNo;

    @Column
    private String parentChannel;

    public String getChannelNo() {
        // 不区分大小写
        return channelNo.toUpperCase();
    }

}
