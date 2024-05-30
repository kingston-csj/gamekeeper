package jforgame.admin.gamecmd.io;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqExecCommand {

    private String selectedServers;

    private int type;

    private String params;

}
