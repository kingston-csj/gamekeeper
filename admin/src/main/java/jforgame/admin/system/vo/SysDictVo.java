package jforgame.admin.system.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysDictVo {

    private Long id;

    private String value;

    private String label;

    private String type;

    private String description;

    private Long sort;

    private String remarks;

}
