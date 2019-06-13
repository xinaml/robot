/**
 * 分页传输
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.dto;

/**
 * 分页
 *
 * @author lgq
 * @date 2018/4/15
 **/
public abstract class PageDTO {
    /**
     * 每显示数量
     */
    protected Integer limit = 10;
    /**
     * 当前页
     */
    protected Integer page = 1;


    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return (this.page - 1) >= 0 ? (this.page - 1) : 0;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
