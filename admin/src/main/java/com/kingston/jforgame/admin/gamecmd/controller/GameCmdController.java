package com.kingston.jforgame.admin.gamecmd.controller;

import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;
import com.kingston.jforgame.admin.gamecmd.executor.AsyncTaskManager;
import com.kingston.jforgame.admin.gamecmd.model.TaskInfo;
import com.kingston.jforgame.admin.gamecmd.service.GameCmdService;
import com.kingston.jforgame.admin.gamecmd.service.PlayerCmdService;
import com.kingston.jforgame.admin.gamecmd.vo.CommandVo;
import com.kingston.jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.user.model.RoleKInds;
import com.kingston.jforgame.admin.utils.JsonUtil;
import com.kingston.jforgame.admin.utils.SimplyReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gameCmd")
public class GameCmdController {

    @Autowired
    private GameCmdService cmdService;

    @Autowired
    private PlayerCmdService playerCmdService;

    @Autowired
    private AsyncTaskManager asyncTaskManager;


    @RequestMapping(value = "/commands", method = RequestMethod.GET)
    public @ResponseBody
    List<CommandVo> queryCommands() {
        List<CommandVo> vos = new ArrayList<>();

        if (!SecurityUtils.hasAuth(RoleKInds.ADMIN)) {
            return vos;
        }

        for (CmdTypes cmd : CmdTypes.values()) {
            if (cmd.getType() != CmdTypes.TYPE_SERVER) {
                continue;
            }
            CommandVo vo = new CommandVo();
            vo.setName(cmd.getName());
            vo.setParams(cmd.getParams());
            vo.setType(cmd.getId());
            vos.add(vo);
        }
        return vos;
    }


    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    public @ResponseBody
    SimplyReply exec(@RequestParam("selectedServers") String selectedServers,
                     @RequestParam("type") int type,
                     @RequestParam("params") String params) {
        if (StringUtils.isEmpty(selectedServers)) {
            return SimplyReply.valueOfFail("无选中服务器");
        }
        List<Integer> servers = new ArrayList<>();
        String[] serversParam = selectedServers.split(";");
        for (String server : serversParam) {
            servers.add(Integer.parseInt(server));
        }

        CmdTypes cmdType = CmdTypes.queryCmd(type);
        if (cmdType.getType() == CmdTypes.TYPE_SERVER) {
            return SimplyReply.valueOfOk(cmdService.execServerCmd(servers, type, params));
        }
        return SimplyReply.valueOfFail("无效指令");
    }

    @RequestMapping(value = "/simplyPlayer", method = RequestMethod.GET)
    public @ResponseBody
    SimplePlayerQueryResult simplyPlayer(@RequestParam("serverId") int serverId,
                                         @RequestParam("sign") String sign) {
        return playerCmdService.queryPlayerSimple(serverId, sign);
    }


    /**
     * 封号或禁言
     */
    @RequestMapping(path = "banPlayer", method = RequestMethod.POST)
    @ResponseBody
    public SimplyReply banPlayer(@RequestParam("serverId") int serverId,
                                 @RequestParam("banType") int banType,
                                 @RequestParam("uid") long uid,
                                 @RequestParam("endTime") long endTime) {
        if (banType == 1) {
            return playerCmdService.banLogin(serverId, uid, endTime);
        }
        return playerCmdService.banChat(serverId, uid, endTime);
    }

    /**
     * 异步发送命令
     *
     * @param selectedServers
     * @return
     */
    @RequestMapping(value = "/hotSwap2", method = RequestMethod.POST)
    public @ResponseBody
    SimplyReply asyncHotSwap(@RequestParam("selectedServers") String selectedServers) {
        String[] params = selectedServers.split(";");
        List<Integer> servers = new ArrayList<>(params.length);
        for (String param : params) {
            servers.add(Integer.parseInt(param));
        }
        return SimplyReply.valueOfOk(cmdService.asyncHotSwap(servers));
    }

    /**
     * 查询任务状态
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/getTaskStatus", method = RequestMethod.GET)
    public @ResponseBody
    SimplyReply getTaskStatus(@RequestParam("taskId") long taskId) {
        TaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        return SimplyReply.valueOfOk(JsonUtil.object2String(taskInfo));
    }

}
