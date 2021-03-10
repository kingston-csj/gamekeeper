package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;

public abstract class HttpServerAdminCmd extends HttpAdminCmd {

    public HttpServerAdminCmd(ServerInfo serverNode, String param) {
        super(serverNode);
    }
}
