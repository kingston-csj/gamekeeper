package jforgame.admin.gamenode.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateServerNode {

    private Integer id;
    private String name;

    /**
     * 所属机器节点id
     */
    private Integer machineId;

    private Integer port;

    /**
     * http端口
     */
    private int httpPort;

    /**
     * 服务器类型, java/go
     */
    private String serverType;

}
