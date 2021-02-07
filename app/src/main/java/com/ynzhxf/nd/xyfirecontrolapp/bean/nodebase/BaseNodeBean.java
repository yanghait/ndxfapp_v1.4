package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 系统架构树基础节点
 * Created by nd on 2018-07-15.
 */

public class BaseNodeBean extends BaseDataBean{

    /**
     * 节点层级
     */
    private int NodeLevel;

    /**
     * 节点名称
     */
    private String Name;

    /**
     * 父节点ID
     */
    private String ParentID;

    /**
     * 节点类型ID
     */
    private String NodeTypeID;

    public int getNodeLevel() {
        return NodeLevel;
    }

    public void setNodeLevel(int nodeLevel) {
        NodeLevel = nodeLevel;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public String getNodeTypeID() {
        return NodeTypeID;
    }

    public void setNodeTypeID(String nodeTypeID) {
        NodeTypeID = nodeTypeID;
    }
}
