package com.xinaml.robot.common.utils.bean;

import java.lang.reflect.Field;
import java.util.List;

public class BeanInfo {
    public BeanInfo(Object source, Object target) {
        this.source = source;
        this.target = target;
    }

    /**
     * 目标源对象
     */
    private Object source;
    /**
     * 目标对象
     */
    private Object target;
    /**
     * 是否转换日期
     */
    private boolean convertDate = false;
    /**
     * 过滤属性
     */
    private String[] excludes;

    /**
     * 只查属性
     */
    private String[] includes;
    /**
     * 目标类属性
     */
    private List<Field> sourceFields;
    /**
     * 源类属性
     */
    private List<Field> targetFields;


    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getTarget() {
        return target;
    }

    public Class getTargetClass() {
        return this.getTarget().getClass();
    }


    public void setTarget(Object target) {
        this.target = target;
    }

    public boolean isConvertDate() {
        return convertDate;
    }

    public void setConvertDate(boolean convertDate) {
        this.convertDate = convertDate;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public List<Field> getSourceFields() {
        return sourceFields;
    }

    public void setSourceFields(List<Field> sourceFields) {
        this.sourceFields = sourceFields;
    }

    public List<Field> getTargetFields() {
        return targetFields;
    }

    public void setTargetFields(List<Field> targetFields) {
        this.targetFields = targetFields;
    }


    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }
}
