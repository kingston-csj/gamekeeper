package com.kingston.jforgame.admin.channel.model;

import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/24 20:10
 */
public interface Tree {

    /**
     * 返回多叉树的所有结点
     *
     * @return
     */
    List<TreeNode> getTree();

    /**
     * 返回所有的根节点
     *
     * @return
     */
    List<TreeNode> getRoot();


    <E> TreeNode findNode(E nodeId);

    /**
     * 返回所有的子孙节点
     *
     * @return
     */
    <E> List<TreeNode> findChildren(E node);
}
