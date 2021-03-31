package jforgame.admin.gamecmd.service;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.http.BanPlayerChatCmd;
import jforgame.admin.gamecmd.cmd.http.BanPlayerLoginCmd;
import jforgame.admin.gamecmd.cmd.http.QueryPlayerCmd;
import jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpResult;
import jforgame.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public List<PlayerSimpleVo> queryPlayerSimple(int serverId, String sign) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        if (server == null) {
            return Collections.emptyList();
        }
        QueryPlayerCmd cmd = new QueryPlayerCmd(server, sign);
        List<PlayerSimpleVo> vos = cmd.action();
        return vos;
    }

    /**
     * 封号
     */
    public HttpResult banLogin(int serverId, long uid,
                               long endTime) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        BanPlayerLoginCmd cmd = new BanPlayerLoginCmd(server, String.valueOf(uid), String.valueOf(endTime));
        String result = cmd.action();
        return JsonUtil.string2Object(result, HttpResult.class);
    }

    /**
     * 禁言
     */
    public HttpResult banChat(int serverId, long uid, long endTime) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        BanPlayerChatCmd cmd = new BanPlayerChatCmd(server, String.valueOf(uid), String.valueOf(endTime));
        String result = cmd.action();
        return JsonUtil.string2Object(result, HttpResult.class);
    }

}
