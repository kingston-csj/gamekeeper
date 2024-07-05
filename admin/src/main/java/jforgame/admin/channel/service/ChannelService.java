package jforgame.admin.channel.service;

import jakarta.annotation.PostConstruct;
import jforgame.admin.channel.dao.ChannelDao;
import jforgame.admin.channel.domain.Channel;
import jforgame.admin.channel.model.ChannelTree;
import jforgame.admin.channel.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChannelService {

    @Autowired
    private ChannelDao channelDao;

    private ChannelTree channelTree;

    @PostConstruct
    private void initChannel() {
        List<Channel> nodes = channelDao.findAll();
        channelTree = new ChannelTree(nodes);
    }

    public Channel findChannelByUserName(String name) {
        return channelDao.findChannelByUserName(name);
    }

    public List<String> queryChildChannel(String channelCode) {
        List<String> result = new ArrayList<>();
        List<TreeNode> nodes = channelTree.findChildren(channelCode);
        for (TreeNode node : nodes) {
            result.add(node.getName().toString());
        }
        return result;
    }

    public List<String> queryAllChannels() {
        return channelTree.getTree().stream().map(ele -> (String)ele.getName()).collect(Collectors.toList());
    }

}
