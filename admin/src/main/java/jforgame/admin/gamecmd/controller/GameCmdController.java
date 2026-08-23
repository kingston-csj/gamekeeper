package jforgame.admin.gamecmd.controller;

import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.admin.gamecmd.executor.AsyncTaskManager;
import jforgame.admin.gamecmd.io.ReqBanPlayer;
import jforgame.admin.gamecmd.model.TaskInfo;
import jforgame.admin.gamecmd.service.GameCmdService;
import jforgame.admin.gamecmd.service.PlayerCmdService;
import jforgame.admin.gamecmd.io.CommandVo;
import jforgame.admin.gamecmd.io.ReqExecCommand;
import jforgame.admin.http.HttpResult;
import jforgame.admin.http.PageResult;
import jforgame.commons.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * @return 0为运营命令，1为运维命令
     */
    @RequestMapping(value = "/commands", method = RequestMethod.GET)
    public List<CommandVo> queryCommands(@RequestParam int type) {
        List<CommandVo> vos = new ArrayList<>();
        for (CmdTypes cmd : CmdTypes.values()) {
            if (cmd.getType() != type) {
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
    public HttpResult exec(@RequestBody ReqExecCommand req) {
        if (StringUtils.isEmpty(req.getSelectedServers())) {
            return HttpResult.error("无选中服务器");
        }
        List<Integer> servers = new ArrayList<>();
        String[] serversParam = req.getSelectedServers().split(";");
        for (String server : serversParam) {
            servers.add(Integer.parseInt(server));
        }

        CmdTypes cmdType = CmdTypes.queryCmd(req.getType());
        if (cmdType.getType() == CmdTypes.TYPE_GAME || cmdType.getType() == CmdTypes.TYPE_SERVER) {
            return HttpResult.ok(cmdService.execServerCmd(servers, req.getType(), req.getParams()));
        }
        return HttpResult.error("无效指令");
    }

    @GetMapping(value = "/simplyPlayer")
    public HttpResult simplyPlayer(@RequestParam int serverId,
                                   @RequestParam String sign,
                                   @RequestParam(defaultValue = "1") int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        PageResult pageResult = playerCmdService.queryPlayerSimplePage(serverId, sign, pageNum, pageSize);
        return HttpResult.ok(pageResult);
    }

    @GetMapping(value = "/simplyGuild")
    public HttpResult simplyGuild(@RequestParam int serverId,
                                   @RequestParam String sign,
                                   @RequestParam(defaultValue = "1") int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        PageResult pageResult = playerCmdService.queryGuildSimplePage(serverId, sign, pageNum, pageSize);
        return HttpResult.ok(pageResult);
    }
    /**
     * 封号或禁言
     */
    @PostMapping(value = "/banPlayer")
    public HttpResult banPlayer(@RequestBody ReqBanPlayer req) {
        if (req.getServerId() <= 0) {
            return HttpResult.error("无选择服务器");
        }
        if (req.getBanType() == 1) {
            return playerCmdService.banLogin(req.getServerId(), req.getUid(), req.getEndTime());
        }
        return playerCmdService.banChat(req.getServerId(), req.getUid(), req.getEndTime());
    }

    /**
     * 异步发送命令
     *
     * @param selectedServers
     * @return
     */
    @RequestMapping(value = "/hotSwap2", method = RequestMethod.POST)
    public HttpResult asyncHotSwap(@RequestParam("selectedServers") String selectedServers) {
        String[] params = selectedServers.split(";");
        List<Integer> servers = new ArrayList<>(params.length);
        for (String param : params) {
            servers.add(Integer.parseInt(param));
        }
        return HttpResult.ok(cmdService.asyncHotSwap(servers));
    }

    /**
     * 查询任务状态
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/getTaskStatus", method = RequestMethod.GET)
    public HttpResult getTaskStatus(@RequestParam("taskId") long taskId) {
        TaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        return HttpResult.ok(JsonUtil.object2String(taskInfo));
    }

}
