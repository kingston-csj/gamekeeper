package jforgame.admin.machinenode.service;

import jforgame.admin.domain.MachineInfo;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.monitor.server.service.MonitorService;
import jforgame.admin.machinenode.dao.MachineInfoDao;
import jforgame.admin.machinenode.io.MachineNodeInfo;
import jforgame.admin.machinenode.io.MachineNodeInfoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineNodeService {


    private static final String CACHE_NAME = "machineNodeCache";

    @Autowired
    private MachineInfoDao machineInfoDao;

    @Autowired
    private MonitorService monitorService;

    /**
     * 获取所有服节点
     *
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'list'")
    public MachineNodeInfoList getServerNodeList() {
        MachineNodeInfoList serverList = new MachineNodeInfoList();
        int totalCount = getServerNodeSum();
        List<MachineInfo> servers = machineInfoDao.findAll();
        List<MachineNodeInfo> vos = new ArrayList<>(servers.size());
        for (MachineInfo server : servers) {
            MachineNodeInfo vo = new MachineNodeInfo();
            vo.setId(server.getId());
            vo.setName(server.getName());
            vo.setIp(server.getIp());
            vo.setAgentUrl(server.getAgentUrl());
            vo.setLogRoot(server.getLogRoot());

            vos.add(vo);
        }

        serverList.setTotalCount(totalCount);
        serverList.setServers(vos);
        return serverList;
    }

    @Cacheable(value = CACHE_NAME, key = "'sum'")
    public int getServerNodeSum() {
        return (int) machineInfoDao.count();
    }



    @Cacheable(value = CACHE_NAME, key = "#id")
    public MachineInfo getMachineNodeBy(Integer id) {
        if (id == null) {
            return null;
        }
        return machineInfoDao.findById(id).orElse(null);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void saveNode(int id, String name, String ip, String agentUrl, String logRoot) {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setId(id);
        machineInfo.setName(name.trim());
        machineInfo.setIp(ip.trim());
        machineInfo.setAgentUrl(agentUrl.trim());
        machineInfo.setLogRoot(logRoot.trim());
        machineInfoDao.save(machineInfo);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public boolean deleteNode(int id) {
        machineInfoDao.deleteById(id);
        return true;
    }

}
