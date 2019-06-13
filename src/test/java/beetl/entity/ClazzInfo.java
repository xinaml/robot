package beetl.entity;

import com.xinaml.frame.common.utils.StringUtil;

public class ClazzInfo {
    private String dir; //目录
    private String packages; //类所在包
    private String className; //类名
    private String author; //作者
    private String des;//描述
    private String version;//版本
    private String tableName; //表名
    private String objName; //对象名

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjName() {
        return StringUtil.toLowerFirst(this.className);
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }
}
