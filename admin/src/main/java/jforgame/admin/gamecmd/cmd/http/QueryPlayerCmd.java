package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.admin.gamecmd.vo.PlayerSimpleVo;

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
//        String url = url() + "?sign=" + sign;
//        String json = httpGet(url);
//        SimplyReply simplyReply = JsonUtil.string2Object(vo, SimplyReply.class);
//        return JsonUtil.string2Collection(simplyReply.getMsg(), ArrayList.class, PlayerSimpleVo.class);
        List<PlayerSimpleVo> list = new ArrayList<>();
        PlayerSimpleVo vo = new PlayerSimpleVo();
        vo.setName("Lily");
        vo.setAccount("hello");
        vo.setLevel(999);
        vo.setMoney(99999);
        vo.setServerId(3);
        list.add(vo);
        return list;
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.QUERY_PLAYER;
    }
}
