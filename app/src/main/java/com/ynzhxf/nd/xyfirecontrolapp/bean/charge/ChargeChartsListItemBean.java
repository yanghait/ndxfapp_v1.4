package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


/**
 * author hbzhou
 * date 2019/10/29 14:23
 */
public class ChargeChartsListItemBean {
    private String name;
    private int unfinished;
    private int finished;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(int unfinished) {
        this.unfinished = unfinished;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
