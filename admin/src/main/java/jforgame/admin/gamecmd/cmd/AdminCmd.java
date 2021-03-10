package jforgame.admin.gamecmd.cmd;

public interface AdminCmd {

    Object action();

    CmdTypes meta();
}
