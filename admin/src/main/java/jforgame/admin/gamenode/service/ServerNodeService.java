package jforgame.admin.gamenode.service;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.dao.ServerInfoDao;
import jforgame.admin.gamenode.io.ServerNodeInfo;
import jforgame.admin.gamenode.io.ServerNodeInfoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerNodeService {

    @Autowired
    private ServerInfoDao serverInfoDao;

    public ServerNodeInfoList getServerNodeList() {
        ServerNodeInfoList serverList = new ServerNodeInfoList();
        int totalCount = getServerNodeSum();
        List<ServerInfo> servers = serverInfoDao.findAll();
        List<ServerNodeInfo> vos = new ArrayList<>(servers.size());
        for (ServerInfo server : servers) {
            ServerNodeInfo vo = new ServerNodeInfo();
            vo.setId(server.getId());
            vo.setName(server.getName());
            vo.setIp(server.getIp());
            vo.setHttpPort(server.getHttpPort());
            vos.add(vo);
        }

        serverList.setTotalCount(totalCount);
        serverList.setServers(vos);
        return serverList;
    }

    public int getServerNodeSum() {
        return (int) serverInfoDao.count();
    }

    public ServerInfo getServerNodeBy(Integer id) {
        return serverInfoDao.getOne(id);
    }

    public void saveNode(int id, String name, String ip, int httpPort) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setId(id);
        serverInfo.setIp(ip.trim());
        serverInfo.setName(name.trim());
        serverInfo.setHttpPort(httpPort);
        serverInfoDao.save(serverInfo);
    }

    public boolean deleteNode(int id) {
        serverInfoDao.deleteById(id);
        return true;
    }

}
