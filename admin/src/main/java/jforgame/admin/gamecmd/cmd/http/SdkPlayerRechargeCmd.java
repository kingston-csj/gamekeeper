package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class SdkPlayerRechargeCmd extends HttpAdminCmd {

    private final String uid;

    private final String money;

    private final String clientData;


    public SdkPlayerRechargeCmd(ServerInfo serverNode, String uid, String money, String clientData) {
        super(serverNode);
        this.uid = uid;
        this.money = money;
        this.clientData = clientData;
    }

    @Override
    public String httpMethod() {
        return "recharge";
    }

    @Override
    public AdminHttpResponse action() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", uid);
        params.put("money", money);
        params.put("app_data", clientData);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return null;
    }
}
