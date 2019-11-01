package com.kingston.jforgame.admin.gamecmd.controller;

import com.kingston.jforgame.admin.gamecmd.service.GameCmdService;
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
    public @ResponseBody SimplyReply hotSwap(@RequestParam("selectedServers") String selectedServers) {
        String[] params = selectedServers.split(";");
        List<Integer> servers = new ArrayList<>(params.length);
        for (String param : params) {
            servers.add(Integer.parseInt(param));
        }
        return SimplyReply.valueOfOk(cmdService.hotSwap(servers));
    }

}
