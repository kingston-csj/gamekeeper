package jforgame.admin.gamecmd.service;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.http.AdminHttpResponse;
import jforgame.admin.gamecmd.cmd.http.BanPlayerChatCmd;
import jforgame.admin.gamecmd.cmd.http.BanPlayerLoginCmd;
import jforgame.admin.gamecmd.cmd.http.QueryGuildCmd;
import jforgame.admin.gamecmd.cmd.http.QueryPlayerCmd;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpResult;
import jforgame.admin.http.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerCmdService {

    @Autowired
    private ServerNodeService serversManager;

    /**
     * 分页查询玩家信息
     * <p>
     * 玩家数据来源于游戏服，由 admin 层对全量结果做内存分页，对外统一返回 PageResult。
     *
     * @param serverId 服务器id
     * @param sign     昵称或角色id
     * @param pageNum  页码，从1开始
     * @param pageSize 每页数量
     */
    public PageResult queryPlayerSimplePage(int serverId, String sign, int pageNum, int pageSize) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        if (server == null) {
            return new PageResult();
        }
        QueryPlayerCmd cmd = new QueryPlayerCmd(server, sign, pageNum, pageSize);
        return cmd.action();
    }


    /**
     * 分页查询公会信息
     * <p>
     * 公会数据来源于游戏服，由 admin 层对全量结果做内存分页，对外统一返回 PageResult。
     *
     * @param serverId 服务器id
     * @param sign     昵称或角色id
     * @param pageNum  页码，从1开始
     * @param pageSize 每页数量
     */
    public PageResult queryGuildSimplePage(int serverId, String sign, int pageNum, int pageSize) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        if (server == null) {
            return new PageResult();
        }
        QueryGuildCmd cmd = new QueryGuildCmd(server, sign, pageNum, pageSize);
        return cmd.action();
    }
    /**
     * 封号
     */
    public HttpResult banLogin(int serverId, long uid,
                               long endTime) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        BanPlayerLoginCmd cmd = new BanPlayerLoginCmd(server, String.valueOf(uid), String.valueOf(endTime));
        AdminHttpResponse result = cmd.action();
        if (result.getCode() == AdminHttpResponse.SUCC) {
            return HttpResult.ok();
        }
        return HttpResult.error(result.getMessage());
    }

    /**
     * 禁言
     */
    public HttpResult banChat(int serverId, long uid, long endTime) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        BanPlayerChatCmd cmd = new BanPlayerChatCmd(server, String.valueOf(uid), String.valueOf(endTime));
        AdminHttpResponse result = cmd.action();
        if (result.getCode() == AdminHttpResponse.SUCC) {
            return HttpResult.ok();
        }
        return HttpResult.error(result.getMessage());
    }

}
