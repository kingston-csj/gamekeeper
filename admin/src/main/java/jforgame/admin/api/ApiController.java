package jforgame.admin.api;

import jforgame.admin.domain.SysDict;
import jforgame.admin.gamenode.io.ServerNodeInfoList;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpResult;
import jforgame.admin.monitor.service.MonitorService;
import jforgame.admin.monitor.vo.ServerJvmSnapshotDto;
import jforgame.admin.system.constant.SysDictConstants;
import jforgame.admin.system.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ServerNodeService serversManager;

    @Autowired
    SysDictService sysDictService;

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/getServerList", method = RequestMethod.GET)
    public HttpResult getServerNodesList() {
        ServerNodeInfoList serverList = serversManager.getServerNodeList();
        return HttpResult.ok(serverList);
    }

    @RequestMapping(value = "/getDirtyWords", method = RequestMethod.GET)
    public HttpResult getDirtyWords() {
        SysDict dict = sysDictService.findById(SysDictConstants.DIRTY_WORDS);
        if (dict == null) {
            return HttpResult.ok("");
        }
        return HttpResult.ok(dict.getValue());
    }

    @PostMapping(value = "/gameInfoUpload")
    public HttpResult updateNodeInfo(
            @RequestParam("serverId") String serverId,
            @RequestBody ServerJvmSnapshotDto dto) {
        monitorService.updateNodeInfo(serverId, dto);
        return HttpResult.ok();
    }

}
