package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

import java.util.List;
import java.util.Map;

public class MaintenListAllBean {
    private String total;
    private String pageNow;
    private String pageCount;
    private String nowPageCount;
    private String totalPageCount;
    private Map<String, Object> footer;
    private List<MaintenListBackBean> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPageNow() {
        return pageNow;
    }

    public void setPageNow(String pageNow) {
        this.pageNow = pageNow;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getNowPageCount() {
        return nowPageCount;
    }

    public void setNowPageCount(String nowPageCount) {
        this.nowPageCount = nowPageCount;
    }

    public String getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(String totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public Map<String, Object> getFooter() {
        return footer;
    }

    public void setFooter(Map<String, Object> footer) {
        this.footer = footer;
    }

    public List<MaintenListBackBean> getRows() {
        return rows;
    }

    public void setRows(List<MaintenListBackBean> rows) {
        this.rows = rows;
    }
}
