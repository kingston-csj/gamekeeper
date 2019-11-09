package com.kingston.jforgame.admin.gamecmd.controller;

import com.kingston.jforgame.admin.gamecmd.executor.AsyncTaskManager;
import com.kingston.jforgame.admin.gamecmd.model.TaskInfo;
import com.kingston.jforgame.admin.gamecmd.service.GameCmdService;
import com.kingston.jforgame.admin.gamecmd.service.PlayerCmdService;
import com.kingston.jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.utils.JsonUtil;
import com.kingston.jforgame.admin.utils.SimplyReply;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/hotSwap", method = RequestMethod.POST)
    public @ResponseBody
    SimplyReply hotSwap(@RequestParam("selectedServers") String selectedServers) {
        String[] params = selectedServers.split(";");
        List<Integer> servers = new ArrayList<>(params.length);
        for (String param : params) {
            servers.add(Integer.parseInt(param));
        }
        return SimplyReply.valueOfOk(cmdService.hotSwap(servers));
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
        if (!SecurityUtils.hasAuth("ADMIN")) {
            return SimplyReply.valueOfFail("权限不够");
        }
        if (banType == 1) {
            return playerCmdService.banLogin(serverId, uid, endTime);
        }
        return playerCmdService.banChat(serverId, uid, endTime);
    }

    /**
     * 异步发送命令
     * @param selectedServers
     * @return
     */
    @RequestMapping(value = "/hotSwap2", method = RequestMethod.POST)
    public @ResponseBody SimplyReply asyncHotSwap(@RequestParam("selectedServers") String selectedServers) {
        String[] params = selectedServers.split(";");
        List<Integer> servers = new ArrayList<>(params.length);
        for (String param : params) {
            servers.add(Integer.parseInt(param));
        }
        return SimplyReply.valueOfOk(cmdService.asyncHotSwap(servers));
    }

    /**
     * 查询任务状态
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/getTaskStatus", method = RequestMethod.GET)
    public @ResponseBody SimplyReply getTaskStatus(@RequestParam("taskId") long taskId) {
        TaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        return SimplyReply.valueOfOk(JsonUtil.object2String(taskInfo));
    }

}
