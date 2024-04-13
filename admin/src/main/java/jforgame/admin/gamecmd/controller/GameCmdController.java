package jforgame.admin.gamecmd.controller;

import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.admin.gamecmd.executor.AsyncTaskManager;
import jforgame.admin.gamecmd.model.TaskInfo;
import jforgame.admin.gamecmd.service.GameCmdService;
import jforgame.admin.gamecmd.service.PlayerCmdService;
import jforgame.admin.gamecmd.vo.CommandVo;
import jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import jforgame.admin.gamecmd.vo.ReqExecCommand;
import jforgame.admin.http.HttpResult;
import jforgame.commons.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("gameCmd")
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
    HttpResult exec(@RequestBody ReqExecCommand req) {
        if (StringUtils.isEmpty(req.getSelectedServers())) {
            return HttpResult.error("无选中服务器");
        }
        List<Integer> servers = new ArrayList<>();
        String[] serversParam = req.getSelectedServers().split(";");
        for (String server : serversParam) {
            servers.add(Integer.parseInt(server));
        }

        CmdTypes cmdType = CmdTypes.queryCmd(req.getType());
        if (cmdType.getType() == CmdTypes.TYPE_SERVER) {
            return HttpResult.ok(cmdService.execServerCmd(servers, req.getType(), req.getParams()));
        }
        return HttpResult.error("无效指令");
    }

    @GetMapping(value = "/simplyPlayer")
    public @ResponseBody
    HttpResult simplyPlayer(@RequestParam int serverId,
                            @RequestParam String sign) {
        List<PlayerSimpleVo> playerInfo = playerCmdService.queryPlayerSimple(serverId, sign);
        return HttpResult.ok(playerInfo);
    }

    /**
     * 封号或禁言
     */
    @RequestMapping(path = "banPlayer", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult banPlayer(@RequestParam int serverId,
                                @RequestParam int banType,
                                @RequestParam long uid,
                                @RequestParam long endTime) {
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
    HttpResult asyncHotSwap(@RequestParam("selectedServers") String selectedServers) {
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
    public @ResponseBody
    HttpResult getTaskStatus(@RequestParam("taskId") long taskId) {
        TaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        return HttpResult.ok(JsonUtil.object2String(taskInfo));
    }

}
