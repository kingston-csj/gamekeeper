package jforgame.admin.gamelog.controller;

import jforgame.admin.gamelog.io.ReqQueryItemLog;
import jforgame.admin.gamelog.service.ExceptionLogService;
import jforgame.admin.gamelog.service.ItemLogService;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.http.QueryLogTypesCmd;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpResult;
import jforgame.commons.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/gamelog")
@ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
public class ItemLogController {

    private static final List<String> DEFAULT_LOG_TYPES = Arrays.asList(
            "login", "quest", "sign", "item", "mail", "activity", "http", "recharge",
            "admin", "hero", "rune", "currency", "guild", "guildbox", "pet", "reset");

    private final ItemLogService itemLogService;
    private final ExceptionLogService exceptionLogService;
    private final ServerNodeService serverNodeService;

    public ItemLogController(ItemLogService itemLogService, ExceptionLogService exceptionLogService, ServerNodeService serverNodeService) {
        this.itemLogService = itemLogService;
        this.exceptionLogService = exceptionLogService;
        this.serverNodeService = serverNodeService;
    }

    @PostMapping("/query")
    public HttpResult query(@RequestBody ReqQueryItemLog req) {
        return HttpResult.ok(itemLogService.queryPage(req));
    }

    @PostMapping("/queryException")
    public HttpResult queryException(@RequestBody ReqQueryItemLog req) {
        return HttpResult.ok(exceptionLogService.queryPage(req));
    }

    @GetMapping("/logTypes")
    public HttpResult queryLogTypes(@RequestParam(defaultValue = "0") int serverId) {
        List<String> types = loadLogTypes(serverId);
        return HttpResult.ok(JsonUtil.object2String(types));
    }

    private List<String> loadLogTypes(int serverId) {
        if (serverId <= 0) {
            return DEFAULT_LOG_TYPES;
        }
        try {
            ServerInfo serverNode = serverNodeService.getServerNodeBy(serverId);
            if (serverNode == null) {
                return DEFAULT_LOG_TYPES;
            }
            List<String> types = new QueryLogTypesCmd(serverNode).action();
            if (types == null || types.isEmpty()) {
                return DEFAULT_LOG_TYPES;
            }
            return types;
        } catch (Exception e) {
            return DEFAULT_LOG_TYPES;
        }
    }
}
