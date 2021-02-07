package com.ynzhxf.nd.xyfirecontrolapp.bean;

import java.io.Serializable;

/**基础实体类
 * Created by nd on 2018-07-12.
 */

public class BaseDataBean implements Serializable,Cloneable{

    /**
     * 实体ID
     */
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

}
