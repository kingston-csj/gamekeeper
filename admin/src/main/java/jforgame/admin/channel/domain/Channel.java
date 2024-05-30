package jforgame.admin.channel.domain;

import jforgame.admin.domain.SysUser;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
