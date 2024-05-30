package jforgame.admin.channel.model;

import jforgame.admin.channel.domain.Channel;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelTree implements Tree {

    private Map<String, TreeNode<String>> channelNodes = new HashMap<>();

    public ChannelTree(List<Channel> nodes) {
        for (Channel node : nodes) {
            TreeNode treeNode = new TreeNode(node.getChannelNo(), null);
            channelNodes.put(node.getChannelNo(), treeNode);
        }

        for (Channel node : nodes) {
            if (!StringUtils.isEmpty(node.getParentChannel())) {
                TreeNode<String> parent = channelNodes.get(node.getParentChannel());
                TreeNode treeNode = new TreeNode(node.getChannelNo(), parent);
                channelNodes.put(node.getChannelNo(), treeNode);
            }
        }

        for (Channel node : nodes) {
            if (!StringUtils.isEmpty(node.getParentChannel())) {
                channelNodes.get(node.getParentChannel()).addChild(channelNodes.get(node.getChannelNo()));
            }
        }
    }

    @Override
    public List<TreeNode> getTree() {
        return new ArrayList<>(channelNodes.values());
    }

    @Override
    public List<TreeNode> getRoot() {
        List<TreeNode> roots = new ArrayList<>();
        for (Map.Entry<String, TreeNode<String>> entry : channelNodes.entrySet()) {
            TreeNode<String> node = entry.getValue();
            if (node.getParent() == null) {
                roots.add(node);
            }
        }
        return roots;
    }

    @Override
    public <String> TreeNode findNode(String nodeId) {
        return channelNodes.get(nodeId);
    }

    @Override
    public <E> List<TreeNode> findChildren(E node) {
        List<TreeNode> result = new ArrayList<>();
        TreeNode<String> treeNode = channelNodes.get(node);
        if (treeNode != null) {
            result.add(treeNode);
            result.addAll(treeNode.getChildren());
            for (TreeNode<String> child : treeNode.getChildren()) {
                result.addAll(child.getChildren());
            }
        }
        return result;
    }
}
