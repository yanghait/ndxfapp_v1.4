package com.ynzhxf.nd.xyfirecontrolapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 树节点
 * Created by nd on 2018-07-23.
 */

public class TreeGridBean<T> implements Serializable , Cloneable{

    //节点ID
    public String id;

    //显示节点文本
    public String name;

    //节点状态，open展开节点，closed收缩节点
    public String state;

    //节点对象
    public T node ;

    //子节点列表
    public List<TreeGridBean<T>> children ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public List<TreeGridBean<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeGridBean<T>> children) {
        this.children = children;
    }
}
