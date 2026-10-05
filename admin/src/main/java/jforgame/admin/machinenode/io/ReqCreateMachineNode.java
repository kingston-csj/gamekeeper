package jforgame.admin.machinenode.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateMachineNode {

    private Integer id;
    private String name;
    private String ip;
    private String agentUrl;
    private String logRoot;

}
