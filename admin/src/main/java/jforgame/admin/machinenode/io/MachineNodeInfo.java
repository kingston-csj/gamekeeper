package jforgame.admin.machinenode.io;

import lombok.Data;

@Data
public class MachineNodeInfo {

    private int id;
    private String name;
    private String ip;
    private String agentUrl;
    private String logRoot;

}
