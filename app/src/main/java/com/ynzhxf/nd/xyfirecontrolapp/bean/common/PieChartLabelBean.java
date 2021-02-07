package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


/**
 * author hbzhou
 * date 2019/2/25 14:51
 */
public class PieChartLabelBean {

    private int color;

    private String label;

    private String eventTypeID;

    public String getEventTypeID() {
        return eventTypeID;
    }

    public void setEventTypeID(String eventTypeID) {
        this.eventTypeID = eventTypeID;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
