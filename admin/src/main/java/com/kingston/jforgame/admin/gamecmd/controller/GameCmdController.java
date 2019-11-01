package com.kingston.jforgame.admin.gamecmd.controller;

import com.kingston.jforgame.admin.gamecmd.service.GameCmdService;
import com.kingston.jforgame.admin.payorder.vo.PayOrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameCmd")
public class GameCmdController {

    @Autowired
    private GameCmdService cmdService;

    @RequestMapping(value = "/hotSwap", method = RequestMethod.GET)
    public @ResponseBody String hotSwap(@RequestParam("selectedServers") String selectedServers) {
        return "执行成功";
    }

}
