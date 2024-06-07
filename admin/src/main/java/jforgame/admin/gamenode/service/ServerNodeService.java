package jforgame.admin.gamenode.service;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.dao.ServerInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerNodeService {

    @Autowired
    private ServerInfoDao serverInfoDao;

    public List<ServerInfo> getServerNodeList(Integer page, Integer count) {
        return serverInfoDao.findAll();
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
        serverInfo.setIp(ip);
        serverInfo.setName(name);
        serverInfo.setHttpPort(httpPort);
        serverInfoDao.save(serverInfo);
    }

    public boolean deleteNode(int id) {
        serverInfoDao.deleteById(id);
        return true;
    }

}
