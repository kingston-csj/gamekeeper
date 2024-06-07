package jforgame.admin.gamenode.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateServerNode {

    private Integer id;
    private String name;
    private String ip;

    private Integer port;

    /**
     * http端口
     */
    private int httpPort;
}
