package jforgame.admin.gamecmd.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqBanPlayer {

    private int serverId;

    private int banType;

    private long uid;

    private long endTime;

}
