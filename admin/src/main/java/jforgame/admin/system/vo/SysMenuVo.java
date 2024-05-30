package jforgame.admin.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysMenuVo {

    private Long id;

    private Long parentId;

    private String name;

    private String url;

    private String perms;

    private Integer type;

    private String icon;

    private Integer orderNum;

    private String parentName;

    private Integer level;

    private List<SysMenuVo> children;

}
