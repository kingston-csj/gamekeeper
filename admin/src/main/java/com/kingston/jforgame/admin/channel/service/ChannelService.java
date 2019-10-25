package com.kingston.jforgame.admin.channel.service;

import com.kingston.jforgame.admin.channel.dao.ChannelDao;
import com.kingston.jforgame.admin.channel.domain.Channel;
import com.kingston.jforgame.admin.channel.model.ChannelTree;
import com.kingston.jforgame.admin.channel.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/24 19:57
 */
@Service
public class ChannelService {

    @Autowired
    private ChannelDao channelDao;

    private ChannelTree channelTree;

    @PostConstruct
    private void initChannel() {
        List<Channel> nodes = channelDao.findAll();

        channelTree = new ChannelTree(nodes);
        System.out.println(channelTree);

    }

    public List<String> queryChildChannel(String channelCode) {
        List<String> result = new ArrayList<>();
        List<TreeNode> nodes = channelTree.findChildren(channelCode);
        for (TreeNode node : nodes) {
            result.add(node.getName().toString());
        }
        return result;
    }

}
