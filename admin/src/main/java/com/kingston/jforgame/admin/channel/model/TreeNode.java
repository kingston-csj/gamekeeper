package com.kingston.jforgame.admin.channel.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/24 20:06
 */
public class TreeNode<E> {

    private E name;

    private TreeNode<E> parent;

    private List<TreeNode<E>> children = new ArrayList<>();

    public TreeNode(E name, TreeNode<E> parent) {
        this.name = name;
        this.parent = parent;
    }

    public E getName() {
        return name;
    }

    public void setName(E name) {
        this.name = name;
    }

    public TreeNode<E> getParent() {
        return parent;
    }

    public void setParent(TreeNode<E> parent) {
        this.parent = parent;
    }

    public void addChild(TreeNode<E> child) {
        this.children.add(child);
    }

    public List<TreeNode<E>> getChildren() {
        return children;
    }
}
