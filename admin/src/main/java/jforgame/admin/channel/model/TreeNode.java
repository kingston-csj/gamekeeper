package jforgame.admin.channel.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TreeNode<E> {

    private E name;

    private TreeNode<E> parent;

    private List<TreeNode<E>> children = new ArrayList<>();

    public TreeNode(E name, TreeNode<E> parent) {
        this.name = name;
        this.parent = parent;
    }

    public void addChild(TreeNode<E> child) {
        this.children.add(child);
    }
}
