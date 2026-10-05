package jforgame.admin.gamenode.service;

import jforgame.admin.domain.MachineInfo;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.dao.ServerInfoDao;
import jforgame.admin.gamenode.io.ServerNodeInfo;
import jforgame.admin.gamenode.io.ServerNodeInfoList;
import jforgame.admin.machinenode.service.MachineNodeService;
import jforgame.admin.monitor.server.service.MonitorService;
import jforgame.admin.monitor.server.vo.ServerJvmMetricsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerNodeService {

    private static final String CACHE_NAME = "serverNodeCache";

    @Autowired
    private ServerInfoDao serverInfoDao;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private MachineNodeService machineNodeService;

    /**
     * 获取所有服节点
     *
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'list'")
    public ServerNodeInfoList getServerNodeList() {
        ServerNodeInfoList serverList = new ServerNodeInfoList();
        int totalCount = getServerNodeSum();
        List<ServerInfo> servers = serverInfoDao.findAll();
        List<ServerNodeInfo> vos = new ArrayList<>(servers.size());
        for (ServerInfo server : servers) {
            MachineInfo machine = machineNodeService.getMachineNodeBy(server.getMachineId());
            ServerNodeInfo vo = new ServerNodeInfo();
            vo.setId(server.getId());
            vo.setName(server.getName());
            vo.setMachineId(server.getMachineId());
            vo.setHttpPort(server.getHttpPort());
            vo.setUseGate(server.getUseGate());
            vo.setServerType(server.getServerType());
            vo.setProtocolAPI(String.format("http://%s:%s/protocol-doc/index.html", machine.getIp(), server.getHttpPort()));
            vos.add(vo);
        }

        serverList.setTotalCount(totalCount);
        serverList.setServers(vos);
        return serverList;
    }

    @Cacheable(value = CACHE_NAME, key = "'sum'")
    public int getServerNodeSum() {
        return (int) serverInfoDao.count();
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    public ServerInfo getServerNodeBy(Integer id) {
        return serverInfoDao.findById(id).orElse(null);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void saveNode(int id, String name, Integer machineId, int httpPort, String serverType) {
        MachineInfo machineInfo = machineNodeService.getMachineNodeBy(machineId);
        if (machineInfo == null) {
            throw new IllegalArgumentException("所属机器节点不存在");
        }
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setId(id);
        serverInfo.setName(name.trim());
        serverInfo.setMachineId(machineId);
        serverInfo.setHttpPort(httpPort);
        serverInfo.setServerType(serverType);
        serverInfoDao.save(serverInfo);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public boolean deleteNode(int id) {
        serverInfoDao.deleteById(id);
        return true;
    }

}
