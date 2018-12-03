package com.kingston.jforgame.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingston.jforgame.admin.dao.ServerInfoDao;
import com.kingston.jforgame.admin.domain.ServerInfo;

@Service
@Transactional
public class ServersManager {
    
	@Autowired
    private ServerInfoDao serverInfoDao;

    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    public List<ServerInfo> getArticleByState(Integer page, Integer count) {
        int start = (page - 1) * count;
        return serverInfoDao.findAll();
    }

    public int getArticleCountByState() {
        return (int) serverInfoDao.count();
    }


    public ServerInfo getArticleById(Integer id) {
       return serverInfoDao.findOne(id);
    }

}
