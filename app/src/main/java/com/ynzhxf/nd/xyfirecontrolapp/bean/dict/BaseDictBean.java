package com.ynzhxf.nd.xyfirecontrolapp.bean.dict;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**字典基础实体
 * Created by nd on 2018-07-15.
 */

public class BaseDictBean extends BaseDataBean{
    /**
     * 名称
     */
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
