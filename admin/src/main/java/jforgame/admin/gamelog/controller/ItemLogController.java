package jforgame.admin.gamelog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jforgame.admin.core.SpringContext;
import jforgame.admin.domain.MachineInfo;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.http.QueryLogTypesCmd;
import jforgame.admin.gamelog.io.AgentResponse;
import jforgame.admin.gamelog.io.ReqQueryItemLog;
import jforgame.admin.gamelog.service.ExceptionLogService;
import jforgame.admin.gamelog.service.ItemLogService;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpClientService;
import jforgame.admin.http.HttpResult;
import jforgame.admin.machinenode.service.MachineNodeService;
import jforgame.commons.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gamelog")
@ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
@Slf4j
public class ItemLogController {

    private static final List<String> DEFAULT_LOG_TYPES = Arrays.asList(
            "login", "quest", "sign", "item", "mail", "activity", "http", "recharge",
            "admin", "hero", "rune", "currency", "guild", "guildbox", "pet", "reset");

    private final ItemLogService itemLogService;
    private final ExceptionLogService exceptionLogService;
    private final ServerNodeService serverNodeService;

    @Autowired
    private MachineNodeService machineNodeService;

    @Autowired
    HttpClientService httpClientService;

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

    @GetMapping("/{serverId}/log/**")
    public Object logBrowser(@PathVariable Integer serverId, HttpServletRequest request) throws IOException {
        // 本接口挂在类级 @RequestMapping("/gamelog") 下，requestURI 形如 /gamelog/{serverId}/log/xxx
        String basePath = "/gamelog/" + serverId + "/log";
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        String subPath = "";
        int idx = uri.indexOf(basePath);
        if (idx >= 0) {
            subPath = uri.substring(idx + basePath.length());
            while (subPath.startsWith("/")) {
                subPath = subPath.substring(1);
            }
        }

        // 查询服务节点信息
        ServerInfo serverInfo = serverNodeService.getServerNodeBy(serverId);
        if (serverInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<h3>serverId不存在</h3>");
        }
        MachineInfo machineInfo = machineNodeService.getMachineNodeBy(serverInfo.getMachineId());
        if (machineInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<h3>Agent节点不存在</h3>");
        }
        String logRoot = machineInfo.getLogRoot();
        if (logRoot == null || logRoot.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<h3>该服务器未配置日志根目录</h3>");
        }
        logRoot = logRoot + File.separator + serverInfo.getName().replace("服务器", "");
        Environment environment = SpringContext.getBean(Environment.class);
        String activatedProfile = environment.getProperty("spring.profiles.active");
        // 如果是dev环境，日志根目录下会多一个server源代码目录
        if ("dev".equalsIgnoreCase(activatedProfile)) {
            logRoot = logRoot + File.separator + "server";
        }

        logRoot = logRoot + File.separator + "logs";
        String agentUrl = machineInfo.getAgentUrl();

        // 调用agent的 /api/log_list GET接口
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("log_root", logRoot);
        reqBody.put("sub_path", subPath);
        // 定义返回DTO
        AgentResponse resp = JsonUtil.string2Object(httpClientService.get(agentUrl + "/api/log_list", reqBody), AgentResponse.class);

        if (resp.isOk()) {
            // 返回目录列表，生成HTML，复刻netty的目录页面
            List<Map<String, Object>> entries = resp.getData();
            String currentPath = subPath.isEmpty() ? basePath : basePath + "/" + subPath;
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n")
                    .append("<html><head><meta charset='utf-8' /><title>Log listing server:").append(serverId).append("</title></head><body>\n")
                    .append("<h3>Listing of ").append(currentPath).append("</h3>\n")
                    .append("<ul>");
            if (!subPath.isEmpty()) {
                int slash = subPath.lastIndexOf('/');
                String parentPath = slash > 0 ? basePath + "/" + subPath.substring(0, slash) : basePath;
                html.append("<li><a href=\"").append(parentPath).append("\">..</a></li>\n");
            }
            for (Map<String, Object> item : entries) {
                String name = (String) item.get("name");
                html.append("<li><a href=\"").append(currentPath).append("/").append(name).append("\">").append(name).append("</a></li>\n");
            }
            html.append("</ul></body></html>");

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html.toString());
        } else {
            // log_list返回失败，说明是文件，走下载逻辑
            // 下载接口返回的是流式响应，Content-Length 不可靠（可能为-1），故先调用log_stat查询真实文件大小
            Long fileSize = queryFileSize(agentUrl, logRoot, subPath);

            String downloadApi = agentUrl + "/api/log_download";
            Map<String, Object> downloadParam = new HashMap<>();
            downloadParam.put("log_root", logRoot);
            downloadParam.put("sub_path", subPath);

            CloseableHttpResponse agentRawResp = httpClientService.getRawResponse(downloadApi, downloadParam);
            HttpEntity entity = agentRawResp.getEntity();

            // ========== 判断文件大小 ==========
            final long MAX_INLINE_SIZE = 5 * 1024 * 1024;
            HttpHeaders headers = new HttpHeaders();
            // 关键：把真实文件长度透传给浏览器。
            // 缺省时响应是chunked(没有Content-Length)，浏览器无法判断文件是否传输完整，
            // 下载会一直处于未完成状态，表现为"有大小但内容为空"
            long contentLength = entity.getContentLength();
            if (contentLength < 0 && fileSize != null) {
                contentLength = fileSize;
            }
            if (contentLength >= 0) {
                headers.setContentLength(contentLength);
            }
            InputStreamResource resource = new InputStreamResource(entity.getContent());

            if (fileSize != null && fileSize < MAX_INLINE_SIZE) {
                // 小于1M，内联文本预览
                headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
            } else {
                // >=1M 或者大小未知，直接弹出下载
                String fileName = subPath.substring(subPath.lastIndexOf("/") + 1);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }
    }


    @GetMapping(value = "/api/log/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadLogFile(@RequestParam Integer serverId,
                                                               @RequestParam String subPath) throws IOException {
        ServerInfo serverInfo = serverNodeService.getServerNodeBy(serverId);
        if (serverInfo == null) {
            return ResponseEntity.notFound().build();
        }
        MachineInfo machineInfo = machineNodeService.getMachineNodeBy(serverInfo.getMachineId());
        if (machineInfo == null) {
            return ResponseEntity.notFound().build();
        }
        String logRoot = machineInfo.getLogRoot();
        if (logRoot == null || logRoot.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String agentUrl = machineInfo.getAgentUrl();

        Map<String, Object> params = new HashMap<>();
        params.put("log_root", logRoot);
        params.put("sub_path", subPath);
        // 调用新增的getRawResponse拿到二进制流
        CloseableHttpResponse agentResp = httpClientService.getRawResponse(agentUrl + "/api/log/download", params);
        HttpEntity entity = agentResp.getEntity();

        HttpHeaders headers = new HttpHeaders();
        for (Header header : agentResp.getAllHeaders()) {
            String hName = header.getName();
            if (HttpHeaders.CONTENT_DISPOSITION.equalsIgnoreCase(hName)
                    || HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(hName)
                    || HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(hName)) {
                headers.add(hName, header.getValue());
            }
        }
        InputStreamResource resource = new InputStreamResource(entity.getContent());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }


    /**
     * 调用agent的 /api/log_stat 查询日志文件大小
     *
     * @return 文件字节数，查询失败返回null
     */
    private Long queryFileSize(String agentUrl, String logRoot, String subPath) {
        try {
            Map<String, Object> statParam = new HashMap<>();
            statParam.put("log_root", logRoot);
            statParam.put("sub_path", subPath);
            AgentResponse statResp = JsonUtil.string2Object(httpClientService.get(agentUrl + "/api/log_stat", statParam), AgentResponse.class);
            if (statResp != null && statResp.isOk() && statResp.getData() != null && !statResp.getData().isEmpty()) {
                Object size = statResp.getData().get(0).get("size");
                return size == null ? null : Long.parseLong(size.toString());
            }
        } catch (Exception e) {
            log.warn("查询日志文件大小失败, subPath={}", subPath, e);
        }
        return null;
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
