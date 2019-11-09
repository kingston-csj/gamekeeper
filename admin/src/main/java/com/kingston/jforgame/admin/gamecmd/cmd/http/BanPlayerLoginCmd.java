package com.kingston.jforgame.admin.gamecmd.cmd.http;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;
import com.kingston.jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import com.kingston.jforgame.admin.utils.JsonUtil;
import com.kingston.jforgame.admin.utils.SimplyReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanPlayerLoginCmd extends HttpAdminCmd {

    private String uid;

    private String endTime;

    public BanPlayerLoginCmd(ServerInfo serverNode, String uid, String endTime) {
        super(serverNode);
        this.uid = uid;
        this.endTime = endTime;
    }

    @Override
    public String httpMethod() {
        return "banLogin";
    }

    @Override
    public String action() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", "" + uid);
        params.put("endTime", "" + endTime);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.BAN_LOGIN;
    }
}
