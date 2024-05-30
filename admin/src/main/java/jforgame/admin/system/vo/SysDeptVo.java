package jforgame.admin.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysDeptVo {

    private Long id;
    private String name;
    private Long parentId;
    private Integer orderNum;
    private int level;
    private String parentName;

    private List<SysDeptVo> children;

}
