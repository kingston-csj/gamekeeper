package jforgame.admin.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * @Author: Kinson
 * @Date: 2019/10/26 10:56
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}