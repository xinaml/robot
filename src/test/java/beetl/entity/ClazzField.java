package beetl.entity;

import com.xinaml.frame.common.utils.StringUtil;

public class ClazzField {
    private String type; //类型
    private String name; //属性名
    private String des; //属性描述
    private String annotation;
    private String getMethod;
    private String setMethod;
    private Boolean notNull;

    public ClazzField() {
    }

    public ClazzField( String type, String name,String des) {
        this.type = type;
        this.name = name;
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getGetMethod() {
        return "get"+ StringUtil.toUpperFirst(name);
    }



    public String getSetMethod() {
        return "set"+ StringUtil.toUpperFirst(name);
    }

    public void setSetMethod(String setMethod) {
        this.setMethod = setMethod;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setGetMethod(String getMethod) {
        this.getMethod = getMethod;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }
}
