//package jforgame.admin.gamecmd.cmd.http;
//
//import jforgame.admin.domain.ServerInfo;
//import jforgame.admin.gamecmd.cmd.CmdTypes;
//
//import java.util.HashMap;
//
//public class CloseServerCmd extends HttpServerAdminCmd {
//
//    public CloseServerCmd(ServerInfo serverNode, String params) {
//        super(serverNode, params);
//    }
//
//    @Override
//    public String httpMethod() {
//        return "closeServer";
//    }
//
//    @Override
//    public String action() {
//        String url = url();
//        return httpPost(url, new HashMap<>());
//    }
//
//    @Override
//    public CmdTypes meta() {
//        return CmdTypes.CLOSE_SERVER;
//    }
//}
