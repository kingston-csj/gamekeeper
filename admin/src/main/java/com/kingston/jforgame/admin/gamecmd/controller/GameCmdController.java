package com.kingston.jforgame.admin.gamecmd.controller;

import com.kingston.jforgame.admin.gamecmd.service.GameCmdService;
import com.kingston.jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
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
        return cmdService.queryPlayerSimple(serverId, sign);
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
            return cmdService.banLogin(serverId, uid, endTime);
        }
        return cmdService.banChat(serverId, uid, endTime);
    }

}
