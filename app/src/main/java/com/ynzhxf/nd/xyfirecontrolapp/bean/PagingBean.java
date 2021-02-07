package com.ynzhxf.nd.xyfirecontrolapp.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**分页实体
 * Created by nd on 2018-07-18.
 */

public class PagingBean<T> implements Serializable,Cloneable{
    /**
     * 数据总数
     */
    private int total;

    /**
     * 当前页码
     */
    private int pageNow;

    /**
     * 每页数量
     */
    private int pageCount;

    /**
     * 当前分页的实际数量
     */
    private int nowPageCount;

    /**
     * 总页数
     */
    private int totalPageCount;

    /**
     * 页脚
     */
    private Map<String,String> footer;

    /**
     * 分页集合
     */
    private List<T> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getNowPageCount() {
        return nowPageCount;
    }

    public void setNowPageCount(int nowPageCount) {
        this.nowPageCount = nowPageCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public Map<String, String> getFooter() {
        return footer;
    }

    public void setFooter(Map<String, String> footer) {
        this.footer = footer;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
