package jforgame.admin.gamenode.controller;

import jforgame.admin.core.I18nConstants;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.domain.SysDict;
import jforgame.admin.gamenode.dao.ServerInfoDao;
import jforgame.admin.gamenode.io.ReqCreateServerNode;
import jforgame.admin.gamenode.io.ServerNodeInfoList;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpResult;
import jforgame.admin.logger.LoggerFunction;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.system.constant.SysDictConstants;
import jforgame.admin.system.service.SysDictService;
import jforgame.commons.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/server")
@Slf4j
public class ServersController {

    @Autowired
    private ServerNodeService serversManager;

    @Autowired
    private ServerInfoDao serverInfoDao;


    @Autowired
    SysDictService sysDictService;

    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public HttpResult getServerNodesList() {
        ServerNodeInfoList serverList = serversManager.getServerNodeList();
        return HttpResult.ok(serverList);
    }

    @PostMapping(value = "/saveNode")
    public HttpResult saveNode(@RequestBody ReqCreateServerNode req) {
        try {
            serversManager.saveNode(req.getId(), req.getName(), req.getIp(), req.getHttpPort());
            LoggerUtil.info(LoggerFunction.SERVER_NODE, "operator", SecurityUtils.getUsername(), "type", "save", "params", JsonUtil.object2String(req));
            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteNode")
    public HttpResult deleteNode(@RequestParam("id") int id) {
        if (id <= 0) {
            return HttpResult.error(I18nConstants.COMMON_NOT_FOUND);
        }
        LoggerUtil.info(LoggerFunction.SERVER_NODE, "operator", SecurityUtils.getUsername(), "type", "delete", "params", id);
        serversManager.deleteNode(id);
        return HttpResult.ok();
    }


    @RequestMapping(value = "/serverIds", method = RequestMethod.GET)
    public Map<String, Object> queryServerIds() {
        Map<String, Object> result = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        List<ServerInfo> servers = serverInfoDao.findAll();

        servers.forEach(server -> {
            if (server.getMerged() <= 0) {
                ids.add(server.getId());
            }
        });

        result.put("ids", servers);
        return result;
    }

    @GetMapping(value = "/monitor")
    public Map<String, String> serverInfo() {
        Map<String, String> result = new HashMap<>();

        result.put("userInfo", "2人");
        result.put("memory", "1g/2g");
        return result;
    }

    @PostMapping("/uploadWords")
    public HttpResult uploadSensitiveExcel(@RequestParam("txtFile") MultipartFile txtFile) {
        if (txtFile.isEmpty()) {
            return HttpResult.error("上传文件不能为空");
        }
        List<String> wordList;
        try {
            wordList = readTxtByLine(txtFile);
        } catch (IOException e) {
            log.error("读取TXT文件失败", e);
            return HttpResult.error("文件读取异常，请检查文件编码");
        }
        if (wordList.isEmpty()) {
            return HttpResult.error("TXT文件内无有效屏蔽词数据");
        }
        // 去空行、去首尾空格
        List<String> filterList = wordList.stream()
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .distinct()
                .toList();
        String wordStr = String.join("\n", filterList);
        log.info("更新敏感词成功，大小为：{}", wordStr.length());

        SysDict dict = new SysDict();
        dict.setId(SysDictConstants.DIRTY_WORDS);
        dict.setValue(wordStr);
        dict.setDescription("敏感词");
        sysDictService.save(dict);

        return HttpResult.ok("屏蔽词更新成功，共加载" + filterList.size() + "个词汇");
    }

    @GetMapping("/exportWords")
    public ResponseEntity<ByteArrayResource> exportDirtyWords() {
        SysDict dict = sysDictService.findById(SysDictConstants.DIRTY_WORDS);
        String content = dict == null || dict.getValue() == null ? "" : dict.getValue();
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        String fileName = "dirty_words_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".txt";
        return toBytesResponse(data, fileName);
    }

    private List<String> readTxtByLine(MultipartFile file) throws IOException {
        List<String> lineList = new ArrayList<>();
        // 使用UTF-8读取，若你的文件是GBK可改为 StandardCharsets.GBK
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }
        }
        return lineList;
    }

    private ResponseEntity<ByteArrayResource> toBytesResponse(byte[] data, String fileName) {
        ByteArrayResource byteData = new ByteArrayResource(data);
        HttpHeaders headers = new HttpHeaders();
        String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(byteData);
    }

}
