package com.kingston.jforgame.admin.gamecmd.cmd.http;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;
import com.kingston.jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import com.kingston.jforgame.admin.utils.JsonUtil;
import com.kingston.jforgame.admin.utils.SimplyReply;

import java.util.ArrayList;
import java.util.List;

public class QueryPlayerCmd extends HttpAdminCmd {

    private String sign;

    public QueryPlayerCmd(ServerInfo serverNode, String sign) {
        super(serverNode);
        this.sign = sign;
    }

    @Override
    public String httpMethod() {
        return "simplePlayer";
    }

    @Override
    public List<PlayerSimpleVo> action() {
        String url = url() + "?sign=" + sign;
        String json = httpGet(url);
        SimplyReply simplyReply = JsonUtil.string2Object(json, SimplyReply.class);
        return JsonUtil.string2Collection(simplyReply.getMsg(), ArrayList.class, PlayerSimpleVo.class);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.QUERY_PLAYER;
    }
}
