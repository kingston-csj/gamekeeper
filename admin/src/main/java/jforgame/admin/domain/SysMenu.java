package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jforgame.admin.system.vo.SysMenuVo;
import lombok.Data;
import jakarta.persistence.Id;

@Entity(name="sys_menu")
@Data
public class SysMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="parent_id")
    private Long parentId;

    @Column
    private String name;

    @Column
    private String url;

    @Column
    private String perms;

    @Column
    private Integer type;

    @Column
    private String icon;

    @Column(name="order_num")
    private Integer orderNum;

    public SysMenuVo simpleView() {
        SysMenuVo vo = new SysMenuVo();
        vo.setId(getId());
        vo.setIcon(getIcon());
        vo.setName(getName());
        vo.setOrderNum(getOrderNum());
        vo.setType(getType());
        vo.setUrl(getUrl());
        vo.setPerms(getPerms());

        return vo;
    }
}
