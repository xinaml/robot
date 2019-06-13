/**
 * 数据传输基类
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据传输对象基类
 */
public abstract class BaseDTO extends PageDTO implements Serializable {
    /**
     * 排序字段 (有排序字段默认排序) "username=desc" 不指定 username (默认使用desc)
     */
    protected List<String> sorts = new ArrayList<>(0);
    /**
     * 类搜索条件
     */
    protected List<Restrict> restricts = new ArrayList<Restrict>(0);
    /** js传递前encodeURI(url)
     * 类搜索条件Json {"field":"name","restrict":"EQ","value":"liguiqin"}
     * 多条件Json [{"field":"name","restrict":"EQ","value":"liguiqin"},{"field":"name","restrict":"IN","value":["liguiqin1","liguiqin2"]}]
     */

    protected String serId;

    protected String restrictsJson;

    public List<String> getSorts() {
        return sorts;
    }

    public void setSorts(List<String> sorts) {
        this.sorts = sorts;
    }

    public List<Restrict> getRestricts() {
        return restricts;
    }

    public void setRestricts(List<Restrict> restricts) {
        this.restricts = restricts;
    }

    public String getRestrictsJson() {
        return restrictsJson;
    }

    public void setRestrictsJson(String restrictsJson) {
        if (null != restrictsJson) {
            try {
                if (restrictsJson.indexOf("[{") != -1 && restrictsJson.lastIndexOf("}]") != -1) {
                    restricts = JSON.parseArray(restrictsJson, Restrict.class);
                } else {
                    restricts = Arrays.asList(JSON.parseObject(restrictsJson, Restrict.class));
                }
            } catch (Exception e) {
                throw new RuntimeException("条件json转换错误");
            }
        }
    }

    /**
     * 添加条件
     */
    public void addRT(Restrict restrict) {
        this.restricts.add(restrict);
    }

    /**
     * 添加排序
     */
    public void addSort(String... sorts) {
        this.sorts.addAll(Arrays.asList(sorts));
    }

    public String getSerId() {
        return JSON.toJSONString(this.restricts)+page;
    }


}
