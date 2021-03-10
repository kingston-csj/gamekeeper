package jforgame.admin.gamecmd.service;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.http.BanPlayerChatCmd;
import jforgame.admin.gamecmd.cmd.http.BanPlayerLoginCmd;
import jforgame.admin.gamecmd.cmd.http.QueryPlayerCmd;
import jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.user.model.RoleKinds;
import jforgame.admin.utils.JsonUtil;
import jforgame.admin.utils.SimplyReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerCmdService {

    @Autowired
    private ServerNodeService serversManager;

    /**
     * @param serverId
     * @param sign     昵称或角色id
     * @return
     */
    public SimplePlayerQueryResult queryPlayerSimple(int serverId, String sign) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        SimplePlayerQueryResult result = new SimplePlayerQueryResult();

        if (!SecurityUtils.hasAuth(RoleKinds.ADMIN)) {
            return result;
        }
        if (server != null) {
            QueryPlayerCmd cmd = new QueryPlayerCmd(server, sign);
            List<PlayerSimpleVo> vos = cmd.action();
            result.setVos(vos);
        }
        return result;
    }

    /**
     * 封号
     */
    public SimplyReply banLogin(int serverId, long uid,
                                long endTime) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        BanPlayerLoginCmd cmd = new BanPlayerLoginCmd(server, String.valueOf(uid), String.valueOf(endTime));
        String result = cmd.action();
        return JsonUtil.string2Object(result, SimplyReply.class);
    }

    /**
     * 禁言
     */
    public SimplyReply banChat(int serverId, long uid, long endTime) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        BanPlayerChatCmd cmd = new BanPlayerChatCmd(server, String.valueOf(uid), String.valueOf(endTime));
        String result = cmd.action();
        return JsonUtil.string2Object(result, SimplyReply.class);
    }

}
