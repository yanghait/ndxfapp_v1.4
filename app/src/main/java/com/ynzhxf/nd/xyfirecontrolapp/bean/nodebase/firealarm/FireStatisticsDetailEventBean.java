package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;


import java.util.List;

/**
 * author hbzhou
 * date 2019/1/15 18:58
 */
public class FireStatisticsDetailEventBean {

    /**
     * name : 火灾自动报警系统
     * hostNum : 1
     * eventCount : [["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-1幢14F切电","722"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-2幢2F声光","251"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-2幢1F感烟","52"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢23F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢24F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢25F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢26F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢27F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢28F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢29F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢30F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢31F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢32F感烟","32"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢屋顶感烟","20"],["1","曲靖市-沾益县-沾益区卓越明郡-火灾自动报警系统-火灾自动报警系统-8幢25F水流指示器","13"]]
     */

    private String name;
    private String hostNum;
    private List<List<String>> eventCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostNum() {
        return hostNum;
    }

    public void setHostNum(String hostNum) {
        this.hostNum = hostNum;
    }

    public List<List<String>> getEventCount() {
        return eventCount;
    }

    public void setEventCount(List<List<String>> eventCount) {
        this.eventCount = eventCount;
    }
}
