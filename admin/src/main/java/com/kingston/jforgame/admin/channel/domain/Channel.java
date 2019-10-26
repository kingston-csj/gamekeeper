package com.kingston.jforgame.admin.channel.domain;

import com.kingston.jforgame.admin.security.GrantedAuthorityImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/24 20:02
 */
@Entity(name = "t_channel")
public class Channel implements UserDetails {

    @Column
    @Id
    private String channelNo;

    @Column
    private String parentChannel;

    @Column
    private String password;

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

    public void setParentChannel(String parentChannel) {
        this.parentChannel = parentChannel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthorityImpl("USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return channelNo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
